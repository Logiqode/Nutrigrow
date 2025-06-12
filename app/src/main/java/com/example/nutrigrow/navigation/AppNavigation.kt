package com.example.nutrigrow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.ui.screens.auth.LoginRoute
import com.example.nutrigrow.di.ViewModelFactory // Your REAL factory
import com.example.nutrigrow.ui.screens.auth.AuthViewModel
import com.example.nutrigrow.ui.screens.home.HomeScreen
import androidx.compose.ui.platform.LocalContext

// Defines the routes for navigation, ensuring type safety.
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    // Add other screens here later, e.g., object Register : Screen("register")
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val viewModelFactory = ViewModelFactory.getInstance(context)

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route // The app will start at the Login screen
    ) {
        // Defines the Login screen
        composable(Screen.Login.route) {
            val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
            LoginRoute(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    // Navigate to home and clear the back stack so the user can't go back to login
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Defines the Home screen
        composable(Screen.Home.route) {
            // This is where your main app content will go after login.
            // For now, let's create a simple placeholder.
            HomeScreen()
        }

        // You can add more screens here later
        // composable(Screen.Register.route) { ... }
    }
}