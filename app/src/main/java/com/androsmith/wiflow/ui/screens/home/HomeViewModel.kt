package com.androsmith.wiflow.ui.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.androsmith.wiflow.WiFlowApplication
import com.androsmith.wiflow.data.UserPreferencesRepository
import com.androsmith.wiflow.domain.FtpServerConfig
import com.androsmith.wiflow.domain.FtpServerManager
import com.androsmith.wiflow.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(HomeUiState())


    var uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
        private set

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage


    var ftpServerManager: FtpServerManager = FtpServerManager()

    init {
        getConfig()
        getIpAddress()
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun getConfig() {
        viewModelScope.launch {
            userPreferencesRepository.ftpConfig.collect { newConfig ->
                _uiState.update { currentState ->
                    currentState.copy(config = newConfig)
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

    fun toggleServer(context: Context) {
        if (_uiState.value.config.rootDirectory == FtpServerConfig().rootDirectory) {
            _toastMessage.value = "Choose a directory from settings!"
        }else {
            if (_uiState.value.isRunning) {
                ftpServerManager.stop()
            } else {
                getIpAddress()
                ftpServerManager.start(_uiState.value.config)
                _toastMessage.value = "Server started!"

            }
            _uiState.update { currentState ->
                currentState.copy(
                    isRunning = !currentState.isRunning
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as WiFlowApplication)
                HomeViewModel(application.container.userPreferencesRepository)
            }
        }
    }
}