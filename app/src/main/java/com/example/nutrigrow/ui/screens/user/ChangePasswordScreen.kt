package com.example.nutrigrow.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.di.ViewModelFactory
import com.example.nutrigrow.ui.theme.DarkGray
import com.example.nutrigrow.ui.theme.PrimaryPink
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Eye
import compose.icons.fontawesomeicons.solid.EyeSlash

@Composable
fun ChangePasswordRoute(
    onBackClick: () -> Unit,
    onChangeSuccess: () -> Unit,
    viewModel: UserViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isUpdateSuccess) {
        if (uiState.isUpdateSuccess) {
            viewModel.clearUpdateSuccess()
            onChangeSuccess()
        }
    }

    ChangePasswordScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onSave = { current, new, confirm ->
            // FIX: Call the updated password change function.
            viewModel.updatePassword(current, new, confirm)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    uiState: UserUiState,
    onBackClick: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

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
                text = "Change Password",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Current Password
        Text(
            text = "Current Password",
            fontSize = 14.sp,
            color = DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                    Icon(
                        // FIX: Check the correct boolean
                        imageVector = if (showCurrentPassword) FontAwesomeIcons.Solid.Eye else FontAwesomeIcons.Solid.EyeSlash,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            placeholder = { Text("Enter your Current Password", color = Color.Gray) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // New Password
        Text(
            text = "New Password",
            fontSize = 14.sp,
            color = DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
            // In New Password field
            trailingIcon = {
                IconButton(onClick = { showNewPassword = !showNewPassword }) {
                    Icon(
                        // FIX: Check the correct boolean
                        imageVector = if (showNewPassword) FontAwesomeIcons.Solid.Eye else FontAwesomeIcons.Solid.EyeSlash,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            placeholder = { Text("Enter your New Password", color = Color.Gray) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password
        Text(
            text = "Confirm Password",
            fontSize = 14.sp,
            color = DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            // In Confirm Password field
            trailingIcon = {
                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                    Icon(
                        // FIX: Check the correct boolean
                        imageVector = if (showConfirmPassword) FontAwesomeIcons.Solid.Eye else FontAwesomeIcons.Solid.EyeSlash,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            placeholder = { Text("Enter your New Password", color = Color.Gray) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Save button
        Button(
            onClick = { onSave(currentPassword, newPassword, confirmPassword) },
            enabled = !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryPink
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Save",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}