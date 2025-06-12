package com.example.nutrigrow.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrigrow.models.UpdateUserRequest
import com.example.nutrigrow.models.UserResponse
import com.example.nutrigrow.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update // Use .update for cleaner state changes
import kotlinx.coroutines.launch

data class UserUiState(
    val user: UserResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUpdateSuccess: Boolean = false
)

class UserViewModel(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    // --- REMOVED ---
    // private var authToken: String = ""
    // fun setAuthToken(token: String) { ... }
    // The AuthInterceptor handles the token now.

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // FIX: Call getMe() without the token parameter.
                val response = apiService.getMe()
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        _uiState.update { it.copy(user = user, isLoading = false) }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to load profile") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Unknown error occurred") }
            }
        }
    }

    fun updateProfile(name: String, email: String, gender: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isUpdateSuccess = false) }

            try {
                val request = UpdateUserRequest(name, email, gender)
                // FIX: Call updateUser() without the token parameter.
                val response = apiService.updateUser(request)

                if (response.isSuccessful) {
                    response.body()?.let { updatedUser ->
                        _uiState.update {
                            it.copy(user = updatedUser, isLoading = false, isUpdateSuccess = true)
                        }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to update profile") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Unknown error occurred") }
            }
        }
    }

    // This function looks fine as a placeholder. No changes needed.
    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        // ...
    }

    fun clearUpdateSuccess() {
        _uiState.update { it.copy(isUpdateSuccess = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}