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
import com.example.nutrigrow.ui.theme.NutriGrowTheme
import com.example.nutrigrow.ui.theme.PrimaryPink
import com.example.nutrigrow.ui.theme.SplashBackground
import com.example.nutrigrow.ui.theme.SplashText

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
            .background(SplashBackground) // THEME CHANGE: Use splash screen background color
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Welcome Back!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = SplashText // THEME CHANGE: Use splash screen text color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Login to continue",
                fontSize = 16.sp,
                color = SplashText.copy(alpha = 0.8f) // THEME CHANGE: Use lighter text color
            )
            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                // THEME CHANGE: Customize text field colors
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPink,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.4f),
                    cursorColor = PrimaryPink,
                    focusedTextColor = SplashText,
                    unfocusedTextColor = SplashText,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                // THEME CHANGE: Customize text field colors
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPink,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.4f),
                    cursorColor = PrimaryPink,
                    focusedTextColor = SplashText,
                    unfocusedTextColor = SplashText,
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { onLoginClicked(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink),
                shape = RoundedCornerShape(12.dp), // Adjusted for a softer look
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(text = "Login", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

// THEME CHANGE: Added a themed preview
@Preview(showBackground = true, name = "Themed Login Screen")
@Composable
fun LoginScreenThemedPreview() {
    NutriGrowTheme {
        LoginScreen(
            uiState = LoginUiState(isLoading = false, errorMessage = null),
            onLoginClicked = { _, _ -> },
        )
    }
}