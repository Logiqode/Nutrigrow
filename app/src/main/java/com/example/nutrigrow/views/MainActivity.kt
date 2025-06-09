package com.example.nutrigrow.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.models.UserResponse
import com.example.nutrigrow.ui.theme.NutriGrowTheme
import com.example.nutrigrow.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriGrowTheme { // Replace with your theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileApp()
                }
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val apiResponse by viewModel.apiResponse.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { viewModel.fetchData("test") }) {
            Text("Fetch Data")
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (apiResponse) {
            null -> Text("No data yet. Click the button!")
            else -> Text("API Response: ${apiResponse!!.message}")
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
        imageUrl = "https://via.placeholder.com/150", // You can use a real image URL
        isVerified = false,
        gender = "Male"
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
                onSave = { name, email, gender ->
                    // Update user data
                    currentUser = currentUser.copy(
                        name = name,
                        email = email,
                        gender = gender
                    )
                    // Go back to profile view
                    currentScreen = ProfileScreen.ProfileView

                    // Here you would call your API
                    // viewModel.updateProfile(name, email, gender)
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