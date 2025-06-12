package com.example.nutrigrow.di // Or com.example.nutrigrow.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// This line creates the DataStore instance at the top level.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {

    // Define a key for storing the auth token.
    private val authTokenKey = stringPreferencesKey("auth_token")

    /**
     * Saves the authentication token to DataStore.
     * The `edit` function is a suspend function, making it safe to call from coroutines.
     */
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[authTokenKey] = token
        }
    }

    /**
     * Retrieves the authentication token from DataStore as a Flow.
     * The UI or ViewModel can collect this Flow to react to token changes.
     */
    val authToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[authTokenKey]
        }

    /**
     * Clears the authentication token from DataStore (for logout).
     */
    suspend fun clearAuthToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(authTokenKey)
        }
    }
}