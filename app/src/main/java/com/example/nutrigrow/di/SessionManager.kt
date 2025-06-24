package com.example.nutrigrow.di // Or com.example.nutrigrow.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Instant

// This line creates the DataStore instance at the top level.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {

    // Define keys for storing session data
    private val authTokenKey = stringPreferencesKey("auth_token")
    private val lastLoginTimeKey = longPreferencesKey("last_login_time")
    private val rememberMeKey = booleanPreferencesKey("remember_me")
    private val rememberMeTokenKey = stringPreferencesKey("remember_me_token")
    private val rememberMeExpiryKey = longPreferencesKey("remember_me_expiry")

    /**
     * Saves the authentication token and login timestamp to DataStore.
     */
    suspend fun saveAuthToken(token: String, rememberMe: Boolean = false) {
        val currentTime = Instant.now().epochSecond
        context.dataStore.edit { preferences ->
            preferences[authTokenKey] = token
            preferences[lastLoginTimeKey] = currentTime
            preferences[rememberMeKey] = rememberMe
            
            if (rememberMe) {
                // Set remember me token and expiry (7 days from now)
                preferences[rememberMeTokenKey] = token
                preferences[rememberMeExpiryKey] = currentTime + (7 * 24 * 60 * 60) // 7 days in seconds
            }
        }
    }

    /**
     * Retrieves the authentication token from DataStore as a Flow.
     */
    val authToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[authTokenKey]
        }

    /**
     * Retrieves the last login time from DataStore as a Flow.
     */
    val lastLoginTime: Flow<Long?> = context.dataStore.data
        .map { preferences ->
            preferences[lastLoginTimeKey]
        }

    /**
     * Retrieves the remember me preference from DataStore as a Flow.
     */
    val rememberMe: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[rememberMeKey] ?: false
        }

    /**
     * Retrieves the remember me token from DataStore as a Flow.
     */
    val rememberMeToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[rememberMeTokenKey]
        }

    /**
     * Retrieves the remember me expiry time from DataStore as a Flow.
     */
    val rememberMeExpiry: Flow<Long?> = context.dataStore.data
        .map { preferences ->
            preferences[rememberMeExpiryKey]
        }

    /**
     * Checks if the current session is valid (within 2 hours).
     */
    suspend fun isSessionValid(): Boolean {
        val currentTime = Instant.now().epochSecond
        val lastLogin = lastLoginTime.first() ?: return false
        val twoHoursInSeconds = 2 * 60 * 60 // 2 hours
        return (currentTime - lastLogin) <= twoHoursInSeconds
    }

    /**
     * Checks if remember me token is valid and refreshes it if needed.
     */
    suspend fun checkAndRefreshRememberMe(): String? {
        val currentTime = Instant.now().epochSecond
        val rememberMeEnabled = rememberMe.first()
        val token = rememberMeToken.first()
        val expiry = rememberMeExpiry.first() ?: 0
        
        if (rememberMeEnabled && token != null && currentTime <= expiry) {
            // Token is valid, refresh the expiry time
            context.dataStore.edit { preferences ->
                preferences[rememberMeExpiryKey] = currentTime + (7 * 24 * 60 * 60) // Extend for another 7 days
                preferences[lastLoginTimeKey] = currentTime // Update last login time
            }
            return token
        }
        
        return null
    }

    /**
     * Updates the last login time (for session refresh).
     */
    suspend fun updateLastLoginTime() {
        val currentTime = Instant.now().epochSecond
        context.dataStore.edit { preferences ->
            preferences[lastLoginTimeKey] = currentTime
        }
    }

    /**
     * Clears the authentication token from DataStore (for logout).
     */
    suspend fun clearAuthToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(authTokenKey)
            preferences.remove(lastLoginTimeKey)
            preferences.remove(rememberMeKey)
            preferences.remove(rememberMeTokenKey)
            preferences.remove(rememberMeExpiryKey)
        }
    }

    /**
     * Clears only the remember me data while keeping the current session.
     */
    suspend fun clearRememberMe() {
        context.dataStore.edit { preferences ->
            preferences.remove(rememberMeKey)
            preferences.remove(rememberMeTokenKey)
            preferences.remove(rememberMeExpiryKey)
        }
    }
}

