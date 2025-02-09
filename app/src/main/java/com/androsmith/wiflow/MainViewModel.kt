package com.androsmith.wiflow

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.androsmith.wiflow.data.AppConfigRepository
import com.androsmith.wiflow.ui.navigation.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainViewModel(
    private val appConfigRepository: AppConfigRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow<Screens?>(null)
    val startDestination: StateFlow<Screens?> = _startDestination

    var isInitialized by mutableStateOf(false)
        private set


    init {
        decideStartDestination()
    }

    private fun decideStartDestination() {
        viewModelScope.launch {
            val onboardingCompleted = appConfigRepository.isOnboardingCompleted.first()
            _startDestination.value = if (onboardingCompleted) {
                Screens.Home
            } else {
                Screens.Welcome
            }
            isInitialized = true

        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as WiFlowApplication)
                MainViewModel(application.container.appConfigRepository)
            }
        }
    }
}

