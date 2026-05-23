package com.androsmith.wiflow.ui.screens.home

import com.androsmith.wiflow.domain.FtpServerConfig
import com.androsmith.wiflow.domain.FtpServerState

data class HomeUiState(
    val isRunning: Boolean = false,
    val serverState: FtpServerState = FtpServerState.Stopped,
    val ipAddress: String = "127.0.0.1",
    val config: FtpServerConfig = FtpServerConfig(),
    val address: String = "ftp://${ipAddress}:${config.port}",
    val activeDeviceName: String = "wiflow"
)
