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
        return try {
            ServerSocket(0).use { socket ->
                socket.localPort
            }
        } catch (e: Exception) {
            2221 // Fallback to default port if something goes wrong
        }
    }
}