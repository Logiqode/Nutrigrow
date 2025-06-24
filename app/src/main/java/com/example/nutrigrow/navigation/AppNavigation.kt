package com.example.nutrigrow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.nutrigrow.di.ViewModelFactory
import com.example.nutrigrow.ui.screens.auth.AuthViewModel
import com.example.nutrigrow.ui.screens.auth.LoginRoute
import com.example.nutrigrow.ui.screens.auth.RegisterRoute
import com.example.nutrigrow.ui.screens.main.MainScreen
import com.example.nutrigrow.ui.screens.splash.SplashScreen
import com.example.nutrigrow.ui.screens.user.ChangePasswordRoute
import com.example.nutrigrow.ui.screens.user.EditProfileRoute
import com.example.nutrigrow.ui.screens.user.ProfileViewRoute
import com.example.nutrigrow.ui.screens.user.UserViewModel


// Defines the routes for navigation, ensuring type safety.
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
    object ProfileView : Screen("profile_view")
    object EditProfile : Screen("edit_profile")
    object ChangePassword : Screen("change_password")
}


@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val viewModelFactory = ViewModelFactory.getInstance(context)

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    // Navigate to Login and remove Splash from the back stack
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onAutoLogin = {
                    // Navigate to Main and remove Splash from the back stack
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Login Screen
        composable(Screen.Login.route) {
            val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
            LoginRoute(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Register Screen
        composable(Screen.Register.route) {
            val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
            RegisterRoute(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Main Screen with Bottom Navigation
        composable(Screen.Main.route) {
            val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
            MainScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // FIXED: Move profile screens to the main navigation graph
        // Profile View Screen
        composable(Screen.ProfileView.route) {
            val userViewModel: UserViewModel = viewModel(factory = viewModelFactory)
            ProfileViewRoute(
                viewModel = userViewModel,
                onBackClick = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate(Screen.EditProfile.route) }
            )
        }

        // Edit Profile Screen
        composable(Screen.EditProfile.route) {
            val userViewModel: UserViewModel = viewModel(factory = viewModelFactory)
            EditProfileRoute(
                viewModel = userViewModel,
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        // Change Password Screen
        composable(Screen.ChangePassword.route) {
            val userViewModel: UserViewModel = viewModel(factory = viewModelFactory)
            ChangePasswordRoute(
                viewModel = userViewModel,
                onBackClick = { navController.popBackStack() },
                onChangeSuccess = { navController.popBackStack() }
            )
        }
    }
}