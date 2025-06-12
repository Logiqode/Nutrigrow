package com.example.nutrigrow.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrigrow.api.ApiService
import com.example.nutrigrow.api.RetrofitClient
import com.example.nutrigrow.di.SessionManager
import com.example.nutrigrow.models.LoginRequest
import com.example.nutrigrow.models.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

data class LoginUiState(
    val loginResponse: LoginResponse? = null,
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
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val request = LoginRequest(email, password)
                val response: Response<LoginResponse> = apiService.login(request)

                if (response.isSuccessful) {
                    response.body()?.let { loginData ->
                        _uiState.value = _uiState.value.copy(
                            loginResponse = loginData,
                            isLoading = false
                        )
                    } ?: run {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Empty response body"
                        )
                    }
                } else {
                    // You might want to parse the error body here for a more specific message
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Login failed: ${response.message()}"
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

    fun saveTokenAfterLogin(token: String) {
        viewModelScope.launch {
            sessionManager.saveAuthToken(token)
            RetrofitClient.setAuthToken(token)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
