package com.androsmith.wiflow.domain

data class FtpServerConfig(
    val username: String = "Android",
    val password: String = "Android",
    val rootDirectory: String = "/storage/emulated/0/",
    val port: Int = 2221,
    val isAnonymousEnabled: Boolean = false,
)
