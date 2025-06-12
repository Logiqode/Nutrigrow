package com.example.nutrigrow.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.example.nutrigrow.models.UserResponse
import com.example.nutrigrow.ui.theme.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.di.ViewModelFactory
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewRoute(
    viewModel: UserViewModel,
    onBackClick: () -> Unit,
    onNavigateToEdit: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    ProfileViewScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onEditClick = onNavigateToEdit
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewScreen(
    uiState: UserUiState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val user = uiState.user ?: return

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
                text = "Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp)) // Balance the back button
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Profile picture
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = user.imageUrl?.ifEmpty { "https://via.placeholder.com/120" }
                ),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Form fields
        Text(
            text = "Full Name",
            fontSize = 14.sp,
            color = DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = user.name ?: "",
            onValueChange = { },
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.LightGray,
                disabledTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Email",
            fontSize = 14.sp,
            color = DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = user.email ?: "",
            onValueChange = { },
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.LightGray,
                disabledTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Edit Profile button
        Button(
            onClick = onEditClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryPink
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Edit Profile",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}