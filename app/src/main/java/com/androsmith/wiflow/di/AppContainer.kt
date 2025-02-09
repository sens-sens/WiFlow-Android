package com.androsmith.wiflow.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.androsmith.wiflow.data.AppConfigRepository
import com.androsmith.wiflow.data.UserPreferencesRepository

interface AppContainer {
    val userPreferencesRepository: UserPreferencesRepository
    val appConfigRepository: AppConfigRepository
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

}