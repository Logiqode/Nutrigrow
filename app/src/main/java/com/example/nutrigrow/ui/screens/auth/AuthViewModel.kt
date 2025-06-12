package com.example.nutrigrow.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrigrow.api.ApiService
import com.example.nutrigrow.api.RetrofitClient
import com.example.nutrigrow.di.SessionManager
import com.example.nutrigrow.models.LoginData
import com.example.nutrigrow.models.LoginRequest
import com.example.nutrigrow.models.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val loginResponse: LoginData? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val request = LoginRequest(email, password)
                // The response type is now Response<ApiResponse<LoginData>>
                val response = apiService.login(request)

                if (response.isSuccessful) {
                    // Get the 'data' object from the response body
                    response.body()?.data?.let { loginData ->
                        _uiState.update {
                            it.copy(
                                // Store the LoginData object in the state
                                loginResponse = loginData,
                                isLoading = false
                            )
                        }
                    } ?: run {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Empty response data") }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Login failed: ${response.message()}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Unknown error occurred") }
            }
        }
    }

    fun saveTokenAfterLogin(token: String) {
        viewModelScope.launch {
            sessionManager.saveAuthToken(token)
            RetrofitClient.setAuthToken(token)
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearAuthToken()
            RetrofitClient.setAuthToken(null)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
