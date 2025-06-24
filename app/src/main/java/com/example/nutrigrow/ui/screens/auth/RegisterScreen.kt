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
fun RegisterRoute(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Use Scaffold for layout structure and snackbar support
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues -> // Scaffold provides padding
        RegisterScreen(
            modifier = Modifier.padding(paddingValues), // Apply padding
            uiState = uiState,
            onRegisterClicked = authViewModel::register,
            onNavigateToLogin = onNavigateToLogin
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

    // Handle successful registration navigation
    LaunchedEffect(uiState.registerResponse) {
        uiState.registerResponse?.let {
            onRegisterSuccess()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onRegisterClicked: (String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = SplashText
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sign up to get started",
                fontSize = 16.sp,
                color = SplashText.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
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
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
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
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
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
                onClick = { 
                    if (password == confirmPassword) {
                        onRegisterClicked(name, email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink),
                shape = RoundedCornerShape(12.dp),
                enabled = !uiState.isLoading && name.isNotBlank() && email.isNotBlank() && 
                         password.isNotBlank() && password == confirmPassword
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(text = "Register", color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = onNavigateToLogin
            ) {
                Text(
                    text = "Already have an account? Login",
                    color = PrimaryPink,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Themed Register Screen")
@Composable
fun RegisterScreenThemedPreview() {
    NutriGrowTheme {
        RegisterScreen(
            uiState = LoginUiState(isLoading = false, errorMessage = null),
            onRegisterClicked = { _, _, _ -> },
            onNavigateToLogin = { }
        )
    }
}

