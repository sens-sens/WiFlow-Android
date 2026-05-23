package com.androsmith.wiflow.ui.screens.home

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.androsmith.wiflow.R
import com.androsmith.wiflow.WiFlowApplication
import com.androsmith.wiflow.data.UserPreferencesRepository
import com.androsmith.wiflow.domain.FtpServerConfig
import com.androsmith.wiflow.domain.FtpServerState
import com.androsmith.wiflow.domain.FtpService
import com.androsmith.wiflow.domain.FtpStateRepository
import com.androsmith.wiflow.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val ftpStateRepository: FtpStateRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(HomeUiState())


    var uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
        private set

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage


    init {
        getConfig()
        getIpAddress()
        observeServerState()
    }

    private fun observeServerState() {
        viewModelScope.launch {
            ftpStateRepository.serverState.collect { serverState ->
                _uiState.update { currentState ->
                    when (serverState) {
                        is FtpServerState.Running -> {
                            currentState.copy(
                                isRunning = true,
                                serverState = serverState,
                                address = serverState.address,
                                ipAddress = serverState.ip,
                                activeDeviceName = serverState.deviceName
                            )
                        }
                        FtpServerState.Stopped -> {
                            currentState.copy(
                                isRunning = false,
                                serverState = serverState,
                                address = ""
                            )
                        }
                        FtpServerState.Starting -> {
                            currentState.copy(
                                isRunning = false,
                                serverState = serverState
                            )
                        }
                        FtpServerState.Stopping -> {
                            currentState.copy(
                                isRunning = true,
                                serverState = serverState
                            )
                        }
                    }
                }
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun getConfig() {
        viewModelScope.launch {
            userPreferencesRepository.ftpConfig.collect { newConfig ->
                _uiState.update { currentState ->
                    currentState.copy(
                        config = newConfig,
                        // Update activeDeviceName if server is NOT running
                        activeDeviceName = if (!currentState.isRunning && currentState.serverState is FtpServerState.Stopped) 
                            newConfig.deviceName 
                        else 
                            currentState.activeDeviceName
                    )
                }
            }
        }
    }

    fun getIpAddress() {
        _uiState.update { currentState ->

            currentState.copy(
                ipAddress = NetworkUtils.getLocalIpAddress() ?: "localhost"
            )
        }
    }

    fun toggleAnonymousAccess(isEnabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateAnonymousAccess(isEnabled)
        }
    }

    fun toggleServer(context: Context) {
        if (_uiState.value.config.rootDirectory == FtpServerConfig().rootDirectory) {
            _toastMessage.value = context.getString(R.string.choose_directory_toast)
        } else {
            val isRunning = _uiState.value.isRunning
            val state = _uiState.value.serverState

            if (state is FtpServerState.Starting || state is FtpServerState.Stopping) return

            val intent = Intent(context, FtpService::class.java).apply {
                action = if (isRunning) {
                    FtpService.ACTION_STOP
                } else {
                    FtpService.ACTION_START
                }
            }

            if (isRunning) {
                _toastMessage.value = context.getString(R.string.server_stopping_toast)
                context.startService(intent)
            } else {
                _toastMessage.value = context.getString(R.string.server_starting_toast)
                context.startForegroundService(intent)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as WiFlowApplication)
                HomeViewModel(
                    application.container.userPreferencesRepository,
                    application.container.ftpStateRepository
                )
            }
        }
    }
}