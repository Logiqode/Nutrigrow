package com.example.nutrigrow.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.api.ApiService
import com.example.nutrigrow.models.BahanMakananResponse
import com.example.nutrigrow.models.LoginRequest
import com.example.nutrigrow.models.LoginResponse
import com.example.nutrigrow.models.RegisterRequest
import com.example.nutrigrow.models.UpdateUserRequest
import com.example.nutrigrow.models.UserResponse
import com.example.nutrigrow.ui.theme.NutriGrowTheme
import com.example.nutrigrow.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriGrowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // This is the main entry point for the testable app
                    AppEntry()
                }
            }
        }
    }
}

@Composable
fun AppEntry() {
    // This state will determine whether to show the login screen or the success screen
    var isLoggedIn by remember { mutableStateOf(false) }

    if (isLoggedIn) {
        // Show a simple success screen after login
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Login Successful!", style = MaterialTheme.typography.headlineMedium)
        }
    } else {
        // The factory that creates our ViewModel with the fake API service
        val factory = AuthViewModelFactory(FakeApiService())
        // Get the AuthViewModel instance
        val authViewModel: AuthViewModel = viewModel(factory = factory)

        // Show the authentication screen
        AuthScreen(
            authViewModel = authViewModel,
            onLoginSuccess = { authToken ->
                // In a real app, you would save the token and navigate
                println("Logged in with token: $authToken")
                isLoggedIn = true
            }
        )
    }
}

// A simple ViewModelFactory to provide the ApiService to our AuthViewModel
class AuthViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


// This is a fake implementation of your ApiService for testing the UI
class FakeApiService : ApiService {
    override suspend fun getBahanMakanan(): Response<BahanMakananResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun register(request: RegisterRequest): Response<UserResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun login(request: LoginRequest): Response<LoginResponse> {
        // Simulate a network delay
        delay(1000)

        // Simulate a failure if the password is "wrong"
        if (request.password == "wrong") {
            return Response.error(401, "{\"message\":\"Invalid credentials\"}".toResponseBody(null))
        }

        // Simulate a successful login
        val user = UserResponse(id = "123", email = request.email, name = "Test User")
        val response = LoginResponse(
            accessToken = "fake-access-token-12345",
            refreshToken = "fake-refresh-token-67890",
            user = user
        )
        return Response.success(response)
    }

    override suspend fun getMe(token: String): Response<UserResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(token: String, request: UpdateUserRequest): Response<UserResponse> {
        TODO("Not yet implemented")
    }
}
