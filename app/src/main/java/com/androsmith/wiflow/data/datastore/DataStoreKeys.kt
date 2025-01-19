package com.androsmith.wiflow.data.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    val USERNAME = stringPreferencesKey("username")
    val PASSWORD = stringPreferencesKey("password")
    val ROOT_DIRECTORY = stringPreferencesKey("root_directory")
    val IS_ANONYMOUS_ENABLED = booleanPreferencesKey("is_anonymous_enabled")
    val PORT = intPreferencesKey("port")
}