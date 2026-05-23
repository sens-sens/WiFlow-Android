package com.androsmith.wiflow.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.androsmith.wiflow.data.AppConfigRepository
import com.androsmith.wiflow.data.UserPreferencesRepository
import com.androsmith.wiflow.domain.FtpServerManager
import com.androsmith.wiflow.domain.FtpStateRepository

interface AppContainer {
    val userPreferencesRepository: UserPreferencesRepository
    val appConfigRepository: AppConfigRepository
    val ftpServerManager: FtpServerManager
    val ftpStateRepository: FtpStateRepository
}

class DefaultAppContainer(
    dataStore: DataStore<Preferences>
) : AppContainer {
    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(dataStore)
    }
    override val appConfigRepository: AppConfigRepository by lazy {
        AppConfigRepository(dataStore)
    }
    override val ftpServerManager: FtpServerManager by lazy {
        FtpServerManager()
    }
    override val ftpStateRepository: FtpStateRepository by lazy {
        FtpStateRepository()
    }
}