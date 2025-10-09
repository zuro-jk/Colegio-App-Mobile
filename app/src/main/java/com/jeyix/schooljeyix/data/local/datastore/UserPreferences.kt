package com.jeyix.schooljeyix.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val KEY_ACCESSTOKEN = stringPreferencesKey("accessToken")
        private val KEY_SESSIONID = intPreferencesKey("sessionId")
        private val KEY_USER = stringPreferencesKey("user")
        private val gson = Gson()
    }

    suspend fun saveUserData(accessToken: String?, sessionId: Number?, user: UserProfileResponse?) {
        dataStore.edit { prefs ->
            prefs[KEY_ACCESSTOKEN] = accessToken ?: ""
            prefs[KEY_SESSIONID] = sessionId?.toInt() ?: 0
            prefs[KEY_USER] = gson.toJson(user)
        }
    }


    val accessToken: Flow<String?> = dataStore.data.map { prefs ->
        prefs[KEY_ACCESSTOKEN]
    }

    val sessionId: Flow<Int?> = dataStore.data.map { prefs ->
        prefs[KEY_SESSIONID]
    }

    val user: Flow<UserProfileResponse?> = dataStore.data.map { prefs ->
        prefs[KEY_USER]?.let { gson.fromJson(it, UserProfileResponse::class.java) }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}