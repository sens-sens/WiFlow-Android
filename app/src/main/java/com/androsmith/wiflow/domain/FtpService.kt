package com.androsmith.wiflow.domain

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.androsmith.wiflow.MainActivity
import com.androsmith.wiflow.R
import com.androsmith.wiflow.WiFlowApplication
import com.androsmith.wiflow.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

class FtpService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var ftpServerManager: FtpServerManager
    private lateinit var ftpStateRepository: FtpStateRepository
    private lateinit var userPreferencesRepository: com.androsmith.wiflow.data.UserPreferencesRepository
    
    private var jmDNS: JmDNS? = null
    private var multicastLock: WifiManager.MulticastLock? = null
    private val mutex = Mutex()

    override fun onCreate() {
        super.onCreate()
        val appContainer = (application as WiFlowApplication).container
        ftpServerManager = appContainer.ftpServerManager
        ftpStateRepository = appContainer.ftpStateRepository
        userPreferencesRepository = appContainer.userPreferencesRepository
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startServer()
            ACTION_STOP -> stopServer()
        }
        return START_STICKY
    }

    private fun startServer() {
        serviceScope.launch {
            mutex.withLock {
                if (ftpStateRepository.serverState.value !is FtpServerState.Stopped) {
                    Log.d(TAG, "Server not stopped, skipping start")
                    return@withLock
                }
                
                ftpStateRepository.updateState(FtpServerState.Starting)
                
                val config = userPreferencesRepository.ftpConfig.first()
                Log.d(TAG, "Starting server with device name: ${config.deviceName}")
                
                val ip = NetworkUtils.getLocalIpAddress() ?: "127.0.0.1"
                val port = NetworkUtils.findFreePort()

                val updatedConfig = config.copy(port = port)
                ftpServerManager.start(updatedConfig)

                startMdns(ip, config.deviceName, port)

                val state = FtpServerState.Running(ip, port, config.deviceName)
                ftpStateRepository.updateState(state)

                startForeground(NOTIFICATION_ID, createNotification(state.address))
            }
        }
    }

    private fun startMdns(ip: String, deviceName: String, port: Int) {
        try {
            val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
            multicastLock = wifiManager.createMulticastLock("wiflow_mdns_lock").apply {
                setReferenceCounted(true)
                acquire()
            }

            val address = InetAddress.getByName(ip)
            jmDNS = JmDNS.create(address, deviceName)
            
            val serviceInfo = ServiceInfo.create(
                "_ftp._tcp.local.",
                deviceName,
                port,
                "path=/"
            )
            jmDNS?.registerService(serviceInfo)
            Log.d(TAG, "mDNS registered: $deviceName.local on port $port")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting mDNS: ${e.message}", e)
        }
    }

    private fun stopServer() {
        serviceScope.launch {
            mutex.withLock {
                if (ftpStateRepository.serverState.value is FtpServerState.Stopped) {
                    Log.d(TAG, "Server already stopped, skipping stop")
                    return@withLock
                }
                
                ftpStateRepository.updateState(FtpServerState.Stopping)
                ftpServerManager.stop()
                stopMdns()
                ftpStateRepository.updateState(FtpServerState.Stopped)
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
    }

    private fun stopMdns() {
        try {
            jmDNS?.unregisterAllServices()
            jmDNS?.close()
            jmDNS = null
            
            multicastLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            multicastLock = null
            Log.d(TAG, "mDNS stopped")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping mDNS: ${e.message}", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.notification_channel_desc)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(ftpAddress: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, FtpService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text, ftpAddress))
            .setSmallIcon(R.drawable.wifi) // Using existing wifi icon
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .addAction(0, getString(R.string.stop), stopPendingIntent)
            .build()
    }

    companion object {
        const val ACTION_START = "com.androsmith.wiflow.ACTION_START"
        const val ACTION_STOP = "com.androsmith.wiflow.ACTION_STOP"
        private const val CHANNEL_ID = "ftp_server_channel"
        private const val NOTIFICATION_ID = 1
        private const val TAG = "FtpService"
    }
}
