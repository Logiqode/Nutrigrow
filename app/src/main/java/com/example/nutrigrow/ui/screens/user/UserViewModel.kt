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
                val response = apiService.getMe()

                if (response.isSuccessful) {
                    // FIX: Access the user object inside the 'data' field.
                    val user = response.body()?.data
                    if (user != null) {
                        _uiState.update { it.copy(user = user, isLoading = false) }
                    } else {
                        // This can happen if the 'data' field itself is null
                        _uiState.update { it.copy(isLoading = false, errorMessage = "User data is empty") }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to load profile") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Unknown error occurred") }
            }
        }
    }

    // This function is now for updating non-sensitive details.
    fun updateProfileDetails(name: String, email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isUpdateSuccess = false) }
            try {
                // We create a request with only the name and email. Password will be null and thus omitted.
                val request = UpdateUserRequest(name = name, email = email)
                val response = apiService.updateUser(request)

                if (response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        // The actual user is inside the .data property
                        apiResponse.data?.let { updatedUser ->
                            _uiState.update {
                                it.copy(user = updatedUser, isLoading = false, isUpdateSuccess = true)
                            }
                        }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to update profile") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Unknown error") }
            }
        }
    }

    // This function now handles the password change logic but calls the same API endpoint.
    fun updatePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isUpdateSuccess = false) }

            // --- Client-side validation ---
            if (newPassword != confirmPassword) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "New passwords don't match") }
                return@launch
            }
            if (newPassword.length < 6) { // Or whatever your rule is
                _uiState.update { it.copy(isLoading = false, errorMessage = "Password must be at least 6 characters") }
                return@launch
            }
            // NOTE: A real app might also require the 'currentPassword' to be sent to the server for verification.
            // Since your API doesn't specify it, we will proceed.

            try {
                // We create a request with only the new password.
                val request = UpdateUserRequest(password = newPassword)
                val response = apiService.updateUser(request)

                if (response.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, isUpdateSuccess = true) }
                } else {
                    // You could parse the server's error message here for more detail
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to change password") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Unknown error") }
            }
        }
    }

    fun clearUpdateSuccess() {
        _uiState.update { it.copy(isUpdateSuccess = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}