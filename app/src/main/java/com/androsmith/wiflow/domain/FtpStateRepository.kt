package com.androsmith.wiflow.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class FtpServerState {
    object Stopped : FtpServerState()
    object Starting : FtpServerState()
    object Stopping : FtpServerState()
    data class Running(val ip: String, val port: Int, val deviceName: String) : FtpServerState() {
        val address: String get() = "ftp://$ip:$port"
    }
}

class FtpStateRepository {
    private val _serverState = MutableStateFlow<FtpServerState>(FtpServerState.Stopped)
    val serverState: StateFlow<FtpServerState> = _serverState.asStateFlow()

    fun updateState(state: FtpServerState) {
        _serverState.value = state
    }
}
