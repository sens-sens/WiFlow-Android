package com.androsmith.wiflow.ui.screens.home

import com.androsmith.wiflow.domain.FtpServerConfig

data class HomeUiState(
    val isRunning: Boolean = false,
    val ipAddress: String = "127.0.0.1",
    val config: FtpServerConfig = FtpServerConfig()
) {
    val address: String = "ftp://${ipAddress}:${config.port}"
}
