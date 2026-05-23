package com.androsmith.wiflow.utils

import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.ServerSocket

object NetworkUtils {
    fun getLocalIpAddress(): String? {
        return try {
            NetworkInterface.getNetworkInterfaces().asSequence()
                .flatMap { it.inetAddresses.asSequence() }
                .filter { it is Inet4Address && !it.isLoopbackAddress }
                .map { it.hostAddress }
                .firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun findFreePort(): Int {
        val easyPorts = listOf(2222, 3333, 4444, 5555, 6666, 7777, 8888, 9999)
        
        // 1. Try easy ports
        for (port in easyPorts) {
            if (isPortAvailable(port)) return port
        }
        
        // 2. Try random 4-digit ports
        repeat(10) {
            val port = (2000..9999).random()
            if (isPortAvailable(port)) return port
        }

        // 3. Fallback to system assigned port
        return try {
            ServerSocket(0).use { socket ->
                socket.localPort
            }
        } catch (e: Exception) {
            2221 // Fallback to default port if something goes wrong
        }
    }

    private fun isPortAvailable(port: Int): Boolean {
        return try {
            ServerSocket(port).use { true }
        } catch (e: Exception) {
            false
        }
    }
}