package com.example.nutrigrow.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrigrow.ui.theme.PrimaryPink

@Composable
fun LoginRoute(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Use Scaffold for layout structure and snackbar support
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues -> // Scaffold provides padding
        LoginScreen(
            modifier = Modifier.padding(paddingValues), // Apply padding
            uiState = uiState,
            onLoginClicked = authViewModel::login
        )
    }

    // Handle showing the snackbar for errors
    val errorMessage = uiState.errorMessage
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            authViewModel.clearError() // Clear error after showing
        }
    }

    // Handle successful login navigation
    LaunchedEffect(uiState.loginResponse) {
        uiState.loginResponse?.let { loginData ->
            // FIX: Access accessToken from the loginData object
            loginData.accessToken?.let { token ->
                authViewModel.saveTokenAfterLogin(token)
                onLoginSuccess()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onLoginClicked: (String, String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Welcome Back!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Login to continue",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onLoginClicked(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink),
                shape = RoundedCornerShape(8.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(text = "Login", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Preview(name = "Default State", showBackground = true)
@Composable
fun LoginScreenPreview() {
    // You can wrap it in your app's theme if you have one
    // NutriGrowTheme {
    LoginScreen(
        uiState = LoginUiState(isLoading = false, errorMessage = null),
        onLoginClicked = { _, _ -> }, // Do nothing in preview
    )
    // }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    // NutriGrowTheme {
    LoginScreen(
        uiState = LoginUiState(isLoading = true),
        onLoginClicked = { _, _ -> },
    )
    // }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenErrorPreview() {
    // NutriGrowTheme {
    LoginScreen(
        uiState = LoginUiState(errorMessage = "Invalid credentials. Please try again."),
        onLoginClicked = { _, _ -> },
    )
    // }
}