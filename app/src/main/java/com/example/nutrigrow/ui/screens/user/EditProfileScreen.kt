package com.example.nutrigrow.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.nutrigrow.ui.theme.*

// 1. The Stateful "Route" Composable
@Composable
fun EditProfileRoute(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: UserViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Load user profile when the screen is first composed if user is null
    LaunchedEffect(Unit) {
        if (uiState.user == null) {
            viewModel.loadUserProfile()
        }
    }

    // When the update is successful, we navigate back.
    LaunchedEffect(uiState.isUpdateSuccess) {
        if (uiState.isUpdateSuccess) {
            viewModel.clearUpdateSuccess() // Reset the flag
            onSaveSuccess()
        }
    }

    // Handle automatic logout when authentication fails
    LaunchedEffect(uiState.shouldLogout) {
        if (uiState.shouldLogout) {
            viewModel.clearLogoutFlag()
            onBackClick() // Navigate back instead of logout here since we're in a nested screen
        }
    }

    // Pass state down and events up
    EditProfileScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onSave = { name, email ->
            viewModel.updateProfileDetails(name, email)
        }
    )
}

// 2. The Stateless UI Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    uiState: UserUiState,
    onBackClick: () -> Unit,
    onSave: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Edit Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp)) // Balance the back button
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Content based on state
        when {
            uiState.isLoading && uiState.user == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null && uiState.user == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterVertically as Alignment.Horizontal
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onBackClick) {
                            Text("Go Back")
                        }
                    }
                }
            }

            uiState.user != null -> {
                EditProfileContent(
                    user = uiState.user,
                    isLoading = uiState.isLoading,
                    errorMessage = uiState.errorMessage,
                    onSave = onSave,
                    onClearError = { /* You might want to add this to ViewModel */ }
                )
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No user data available")
                }
            }
        }
    }
}

@Composable
private fun EditProfileContent(
    user: com.example.nutrigrow.models.UserResponse,
    isLoading: Boolean,
    errorMessage: String?,
    onSave: (String, String) -> Unit,
    onClearError: () -> Unit
) {
    var name by remember(user.id) { mutableStateOf(user.name ?: "") }
    var email by remember(user.id) { mutableStateOf(user.email ?: "") }

    val isSaveEnabled = !isLoading && name.isNotBlank() && email.isNotBlank()

    Column {
        // Profile picture
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = user.imageUrl?.ifEmpty { "https://via.placeholder.com/120" }
                        ?: "https://via.placeholder.com/120"
                ),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Error message display
        if (errorMessage != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
            ) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Form fields
        Text(
            text = "Full Name",
            fontSize = 14.sp,
            color = DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter your full name") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Email",
            fontSize = 14.sp,
            color = DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter your email") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Save button
        Button(
            onClick = { onSave(name, email) },
            enabled = isSaveEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryPink
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "Save Changes",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}