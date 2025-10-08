package com.jeyix.schooljeyix.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val KEY_TOKEN = stringPreferencesKey("jwt_token")
        private val KEY_EMAIL = stringPreferencesKey("user_email")
    }

    suspend fun saveUserData(token: String, email: String) {
        dataStore.edit { prefs ->
            prefs[KEY_TOKEN] = token
            prefs[KEY_EMAIL] = email
        }
    }

    val token: Flow<String?> = dataStore.data.map { prefs ->
        prefs[KEY_TOKEN]
    }

    val email: Flow<String?> = dataStore.data.map { prefs ->
        prefs[KEY_EMAIL]
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}