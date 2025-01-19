package com.androsmith.wiflow.utils

import java.net.Inet4Address
import java.net.NetworkInterface

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
}