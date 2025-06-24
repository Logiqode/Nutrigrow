package com.example.nutrigrow.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.example.nutrigrow.ui.theme.*
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.nutrigrow.di.ViewModelFactory
import com.example.nutrigrow.navigation.Screen
import androidx.compose.runtime.getValue

@Composable
fun UserProfileRoute(
    viewModel: UserViewModel,
    onNavigate: (String) -> Unit,
    onClose: () -> Unit,
    onLogout: () -> Unit,
) {
    // Fetch user data when the screen is first composed
    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }
    val uiState by viewModel.uiState.collectAsState()

    // Handle automatic logout when authentication fails
    LaunchedEffect(uiState.shouldLogout) {
        if (uiState.shouldLogout) {
            viewModel.clearLogoutFlag()
            onLogout()
        }
    }

    UserProfileScreen(
        uiState = uiState,
        onProfileClick = { onNavigate(Screen.ProfileView.route) },
        onChangePasswordClick = { onNavigate(Screen.ChangePassword.route) },
        onFAQClick = { /* TODO */ },
        onAboutUsClick = { /* TODO */ },
        onTermsClick = { /* TODO */ },
        onLogoutClick = onLogout,
        onCloseClick = onClose
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    uiState: UserUiState,
    onProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onFAQClick: () -> Unit,
    onAboutUsClick: () -> Unit,
    onTermsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header with close button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Account",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onCloseClick) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile picture and name
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.user != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = uiState.user.imageUrl?.ifEmpty { "https://via.placeholder.com/120" }
                    ),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(100.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = uiState.user.name ?: "Unnamed User",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else if (uiState.errorMessage != null) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text(text = uiState.errorMessage)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Menu items
        MenuItemRow("Profile", onProfileClick)
        MenuItemRow("Change Password", onChangePasswordClick)
        MenuItemRow("FAQ", onFAQClick)
        MenuItemRow("About Us", onAboutUsClick)
        MenuItemRow("Term & Condition", onTermsClick)
        MenuItemRow("Log Out", onLogoutClick)
    }
}

@Composable
fun MenuItemRow(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Arrow",
            tint = DarkGray
        )
    }
}


