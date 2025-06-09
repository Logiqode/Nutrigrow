package com.example.nutrigrow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrigrow.models.UpdateProfileRequest
import com.example.nutrigrow.models.UserResponse
import com.example.nutrigrow.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private var authToken: String = ""

    fun setAuthToken(token: String) {
        authToken = "Bearer $token"
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val response = apiService.getMe(authToken)
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        _uiState.value = _uiState.value.copy(
                            user = user,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to load profile"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun updateProfile(name: String, email: String, gender: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val request = UpdateProfileRequest(name, email, gender)
                val response = apiService.updateProfile(authToken, request)

                if (response.isSuccessful) {
                    response.body()?.let { updatedUser ->
                        _uiState.value = _uiState.value.copy(
                            user = updatedUser,
                            isLoading = false,
                            isUpdateSuccess = true
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to update profile"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            // Validate passwords
            if (newPassword != confirmPassword) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "New passwords don't match"
                )
                return@launch
            }

            if (newPassword.length < 6) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Password must be at least 6 characters"
                )
                return@launch
            }

            try {
                // You'll need to add this endpoint to your ApiService
                // val response = apiService.changePassword(authToken, ChangePasswordRequest(currentPassword, newPassword))

                // For now, simulate success
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isUpdateSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to change password"
                )
            }
        }
    }

    fun clearUpdateSuccess() {
        _uiState.value = _uiState.value.copy(isUpdateSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}