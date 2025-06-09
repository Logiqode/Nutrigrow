package com.example.nutrigrow.views

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.ui.theme.PrimaryPink
import com.example.nutrigrow.viewmodel.AuthViewModel
import com.example.nutrigrow.viewmodel.LoginUiState

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel, // Pass your ViewModel instance here
    onLoginSuccess: (String) -> Unit // Callback to pass the auth token
) {
    val uiState by authViewModel.uiState.collectAsState()

    LoginScreen(
        uiState = uiState,
        onLoginClicked = { email, password ->
            authViewModel.login(email, password)
        },
        onErrorDismissed = {
            authViewModel.clearError()
        }
    )

    // Handle successful login
    LaunchedEffect(uiState.loginResponse) {
        uiState.loginResponse?.let {
            onLoginSuccess(it.accessToken)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onLoginClicked: (String, String) -> Unit,
    onErrorDismissed: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
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

        uiState.errorMessage?.let { message ->
            AlertDialog(
                onDismissRequest = onErrorDismissed,
                title = { Text("Login Failed") },
                text = { Text(message) },
                confirmButton = {
                    Button(onClick = onErrorDismissed) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
