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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    user: UserResponse,
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
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile picture and name
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = user.imageUrl.ifEmpty { "https://via.placeholder.com/120" }
                ),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = user.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewScreen(
    user: UserResponse,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
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
                    model = user.imageUrl.ifEmpty { "https://via.placeholder.com/120" }
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
            value = user.name,
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
            value = user.email,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    user: UserResponse,
    onBackClick: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }

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
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Profile picture
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = user.imageUrl.ifEmpty { "https://via.placeholder.com/120" }
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
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Save button
        Button(
            onClick = { onSave(name, email) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
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
//                    Icon(
//                        imageVector = if (showCurrentPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                        contentDescription = if (showCurrentPassword) "Hide password" else "Show password"
//                    )
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
            trailingIcon = {
                IconButton(onClick = { showNewPassword = !showNewPassword }) {
//                    Icon(
//                        imageVector = if (showNewPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                        contentDescription = if (showNewPassword) "Hide password" else "Show password"
//                    )
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
            trailingIcon = {
                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
//                    Icon(
//                        imageVector = if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                        contentDescription = if (showConfirmPassword) "Hide password" else "Show password"
//                    )
                }
            },
            placeholder = { Text("Enter your New Password", color = Color.Gray) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Save button
        Button(
            onClick = { onSave(currentPassword, newPassword, confirmPassword) },
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


sealed class ProfileScreen {
    object Account : ProfileScreen()
    object ProfileView : ProfileScreen()
    object EditProfile : ProfileScreen()
    object ChangePassword : ProfileScreen()
}

@Composable
fun ProfileApp() {
    // Test user data
    val testUser = UserResponse(
        id = "fcecae08-7768-45ce-bca3-6239fbf84cb2",
        name = "Revy Pramana",
        email = "alomani@gmail.com",
        telpNumber = "",
        role = "admin",
        imageUrl = "https://via.placeholder.com/150",
        isVerified = false
    )

    var currentScreen by remember { mutableStateOf<ProfileScreen>(ProfileScreen.Account) }
    var currentUser by remember { mutableStateOf(testUser) }

    when (currentScreen) {
        ProfileScreen.Account -> {
            AccountScreen(
                user = currentUser,
                onProfileClick = { currentScreen = ProfileScreen.ProfileView },
                onChangePasswordClick = { currentScreen = ProfileScreen.ChangePassword },
                onFAQClick = { /* Handle FAQ */ },
                onAboutUsClick = { /* Handle About Us */ },
                onTermsClick = { /* Handle Terms */ },
                onLogoutClick = { /* Handle Logout */ },
                onCloseClick = { /* Handle Close - go back to main app */ }
            )
        }

        ProfileScreen.ProfileView -> {
            ProfileViewScreen(
                user = currentUser,
                onBackClick = { currentScreen = ProfileScreen.Account },
                onEditClick = { currentScreen = ProfileScreen.EditProfile }
            )
        }

        ProfileScreen.EditProfile -> {
            EditProfileScreen(
                user = currentUser,
                onBackClick = { currentScreen = ProfileScreen.ProfileView },
                onSave = { name, email ->
                    // Update user data
                    currentUser = currentUser.copy(
                        name = name,
                        email = email,
                    )
                    // Go back to profile view
                    currentScreen = ProfileScreen.ProfileView

                    // Here you would call your API
                    // viewModel.updateProfile(name, email)
                }
            )
        }

        ProfileScreen.ChangePassword -> {
            ChangePasswordScreen(
                onBackClick = { currentScreen = ProfileScreen.Account },
                onSave = { currentPassword, newPassword, confirmPassword ->
                    // Validate and change password
                    if (newPassword == confirmPassword && newPassword.length >= 6) {
                        // Handle password change
                        currentScreen = ProfileScreen.Account

                        // Here you would call your API
                        // viewModel.changePassword(currentPassword, newPassword, confirmPassword)
                    }
                }
            )
        }
    }
}