package com.androsmith.wiflow.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.androsmith.wiflow.data.datastore.DataStoreKeys
import com.androsmith.wiflow.domain.FtpServerConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

const val TAG = "UserPreferencesRepo"


class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    val defaultConfig = FtpServerConfig()

    val ftpConfig: Flow<FtpServerConfig> = dataStore.data.catch {
        if (it is IOException) {
            Log.e(TAG, "Error reading preferences.", it)
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->

        FtpServerConfig(
            username = preferences[DataStoreKeys.USERNAME] ?: defaultConfig.username,
            password = preferences[DataStoreKeys.PASSWORD] ?: defaultConfig.password,
            rootDirectory = preferences[DataStoreKeys.ROOT_DIRECTORY]
                ?: defaultConfig.rootDirectory,
            isAnonymousEnabled = preferences[DataStoreKeys.IS_ANONYMOUS_ENABLED]
                ?: defaultConfig.isAnonymousEnabled,
            port = preferences[DataStoreKeys.PORT] ?: defaultConfig.port
        )
    }

    suspend fun updateConfig(config: FtpServerConfig) {
        dataStore.edit { preferences ->
            preferences[DataStoreKeys.USERNAME] = config.username
            preferences[DataStoreKeys.PASSWORD] = config.password
            preferences[DataStoreKeys.ROOT_DIRECTORY] = config.rootDirectory
            preferences[DataStoreKeys.IS_ANONYMOUS_ENABLED] = config.isAnonymousEnabled
            preferences[DataStoreKeys.PORT] = config.port
        }
    }

    suspend fun updateDirectory(path: String) {
        dataStore.edit { preferences ->
            preferences[DataStoreKeys.ROOT_DIRECTORY] = path
        }
    }

    suspend fun updateUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[DataStoreKeys.USERNAME] = username
        }
    }

    suspend fun updatePassword(password: String) {
        dataStore.edit { preferences ->
            preferences[DataStoreKeys.PASSWORD] = password
        }
    }

    suspend fun updateAnonymousAccess(isAnonymous: Boolean) {
        dataStore.edit { preferences ->
            preferences[DataStoreKeys.IS_ANONYMOUS_ENABLED] = isAnonymous
        }
    }
}