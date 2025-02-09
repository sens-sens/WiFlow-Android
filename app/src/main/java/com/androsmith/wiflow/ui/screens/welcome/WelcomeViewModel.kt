package com.androsmith.wiflow.ui.screens.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.androsmith.wiflow.MainViewModel
import com.androsmith.wiflow.WiFlowApplication
import com.androsmith.wiflow.data.AppConfigRepository
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val appConfigRepository: AppConfigRepository

) : ViewModel() {

    fun finishOnboarding() {
        viewModelScope.launch {
            appConfigRepository.setOnboardingCompleted(true)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as WiFlowApplication)
                WelcomeViewModel(application.container.appConfigRepository)
            }
        }
    }
}