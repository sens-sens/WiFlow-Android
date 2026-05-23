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
import com.androsmith.wiflow.data.UserPreferencesRepository
import com.androsmith.wiflow.domain.AppTheme
import com.androsmith.wiflow.ui.navigation.Screens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val appConfigRepository: AppConfigRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow<Screens?>(null)
    val startDestination: StateFlow<Screens?> = _startDestination

    private val _themeMode = MutableStateFlow<AppTheme>(AppTheme.SYSTEM)
    val themeMode: StateFlow<AppTheme> = _themeMode.asStateFlow()

    var isInitialized by mutableStateOf(false)


    init {

        viewModelScope.launch {
            appConfigRepository.isOnboardingCompleted.collect { completed ->
                _startDestination.value = if (completed) Screens.Home else Screens.Welcome
                isInitialized = true
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.appTheme.collect {
                _themeMode.value = it
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as WiFlowApplication)
                MainViewModel(
                    application.container.appConfigRepository,
                    application.container.userPreferencesRepository
                )
            }
        }
    }
}
