package com.example.nutrigrow.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrigrow.api.ApiService
import com.example.nutrigrow.api.RetrofitClient
import com.example.nutrigrow.di.SessionManager
import com.example.nutrigrow.models.LoginData
import com.example.nutrigrow.models.LoginRequest
import com.example.nutrigrow.models.LoginResponse
import com.example.nutrigrow.models.RegisterRequest
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

// Add a new UI State for Registration
data class RegisterUiState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)


class AuthViewModel(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Add a new StateFlow for the Register UI
    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val request = LoginRequest(email, password)
                val response = apiService.login(request)

                if (response.isSuccessful) {
                    response.body()?.data?.let { loginData ->
                        _uiState.update {
                            it.copy(
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

    // Add the register function
    fun register(name: String, email: String, telpNumber: String, password: String) {
        viewModelScope.launch {
            _registerUiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

            try {
                val request = RegisterRequest(name = name, email = email, telpNumber = telpNumber, password = password)
                val response = apiService.register(request)

                if (response.isSuccessful && response.body()?.status == true) {
                    _registerUiState.update {
                        it.copy(isLoading = false, isSuccess = true)
                    }
                } else {
                    val errorMessage = response.body()?.message ?: "Registration failed"
                    _registerUiState.update {
                        it.copy(isLoading = false, errorMessage = errorMessage)
                    }
                }
            } catch (e: Exception) {
                _registerUiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "An unknown error occurred")
                }
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

    // Function to reset the register state, e.g., after showing an error or success message
    fun clearRegisterState() {
        _registerUiState.value = RegisterUiState()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}