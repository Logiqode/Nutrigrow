package com.example.nutrigrow.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()

    // The LoginRoute now simply passes state down and events up.
    // The Scaffold and Snackbar have been removed.
    LoginScreen(
        uiState = uiState,
        onLoginClicked = authViewModel::login,
        onNavigateToRegister = onNavigateToRegister,
        onErrorDismiss = authViewModel::clearError // Pass the event handler to clear errors
    )

    // Handle successful login navigation. This remains unchanged.
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
    onNavigateToRegister: () -> Unit,
    onErrorDismiss: () -> Unit // New parameter to handle error dismissal
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // When the user starts typing, clear any previous error messages.
    val onEmailChange = { newEmail: String ->
        onErrorDismiss()
        email = newEmail
    }
    val onPasswordChange = { newPass: String ->
        onErrorDismiss()
        password = newPass
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SplashBackground)
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
                color = SplashText
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Login to continue",
                fontSize = 16.sp,
                color = SplashText.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorMessage != null, // Highlight field if there's an error
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
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorMessage != null, // Highlight field if there's an error
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPink,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.4f),
                    cursorColor = PrimaryPink,
                    focusedTextColor = SplashText,
                    unfocusedTextColor = SplashText,
                )
            )

            // On-screen error message display
            AnimatedVisibility(
                visible = uiState.errorMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = uiState.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onLoginClicked(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink),
                shape = RoundedCornerShape(12.dp),
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

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = SplashText.copy(alpha = 0.8f)
                )
                Text(
                    text = "Register Now",
                    color = PrimaryPink,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Login Screen with Error")
@Composable
fun LoginScreenThemedPreview() {
    NutriGrowTheme {
        LoginScreen(
            uiState = LoginUiState(isLoading = false, errorMessage = "Invalid email or password."),
            onLoginClicked = { _, _ -> },
            onNavigateToRegister = {},
            onErrorDismiss = {}
        )
    }
}