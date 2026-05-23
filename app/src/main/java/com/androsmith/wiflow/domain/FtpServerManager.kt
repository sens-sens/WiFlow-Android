package com.androsmith.wiflow.domain

import android.util.Log
import com.androsmith.wiflow.utils.NetworkUtils
import org.apache.ftpserver.ConnectionConfigFactory
import org.apache.ftpserver.FtpServer
import org.apache.ftpserver.FtpServerFactory
import org.apache.ftpserver.ftplet.UserManager
import org.apache.ftpserver.listener.Listener
import org.apache.ftpserver.listener.ListenerFactory
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory
import org.apache.ftpserver.usermanager.impl.BaseUser
import org.apache.ftpserver.usermanager.impl.WritePermission

class FtpServerManager {

    private lateinit var server: FtpServer

    fun start(config: FtpServerConfig) {
        try {
            server = createServer(config)
            server.start()
            Log.d(TAG, "Server started at ftp://${NetworkUtils.getLocalIpAddress()}:${config.port}")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting server: ${e.message}", e)
        }
    }

    fun stop() {
        try {
            if (::server.isInitialized) {
                server.stop()
                Log.d(TAG, "Server stopped")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping server: ${e.message}", e)
        }
    }

    private fun createServer(config: FtpServerConfig): FtpServer {
        val userManager = createUserManager(config)
        val listener = createListener(config.port)

        return FtpServerFactory().apply {
            this.userManager = userManager
            addListener("default", listener)
            
            if (config.isAnonymousEnabled) {
                val connectionConfigFactory = ConnectionConfigFactory()
                connectionConfigFactory.isAnonymousLoginEnabled = true
                this.connectionConfig = connectionConfigFactory.createConnectionConfig()
            }
        }.createServer()
    }

    private fun createUserManager(config: FtpServerConfig): UserManager {
        val userManagerFactory = PropertiesUserManagerFactory()
        val userManager = userManagerFactory.createUserManager()

        val user = BaseUser().apply {
            name = config.username
            password = config.password
            homeDirectory = config.rootDirectory
            authorities = listOf(WritePermission())
        }
        userManager.save(user)

        if (config.isAnonymousEnabled) {
            val anonymousUser = BaseUser().apply {
                name = "anonymous"
                homeDirectory = config.rootDirectory
                authorities = listOf(WritePermission())
            }
            userManager.save(anonymousUser)
        }

        return userManager
    }

    private fun createListener(port: Int): Listener {
        return ListenerFactory().apply {
            this.port = port
        }.createListener()
    }

    companion object {
        private const val TAG = "FtpServerManager"
    }
}
