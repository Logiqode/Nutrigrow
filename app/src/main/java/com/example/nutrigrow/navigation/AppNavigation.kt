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
import com.example.nutrigrow.ui.screens.home.HomeScreenRoute
import com.example.nutrigrow.ui.screens.user.ChangePasswordRoute
import com.example.nutrigrow.ui.screens.user.EditProfileRoute
import com.example.nutrigrow.ui.screens.user.ProfileViewRoute
import com.example.nutrigrow.ui.screens.user.UserProfileRoute
import com.example.nutrigrow.ui.screens.user.UserViewModel


// Defines the routes for navigation, ensuring type safety.
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object UserProfile : Screen("user_profile")
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
        startDestination = Screen.Login.route
    ) {
        // Login Screen (not part of any nested graph)
        composable(Screen.Login.route) {
            val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
            LoginRoute(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Home Screen (not part of any nested graph)
        composable(Screen.Home.route) {
            HomeScreenRoute(
                onProfileClicked = {
                    navController.navigate("profile_flow")
                }
            )
        }

        // --- NESTED NAVIGATION GRAPH FOR THE PROFILE FEATURE ---
        navigation(
            route = "profile_flow",
            startDestination = Screen.UserProfile.route
        ) {
            // Define all screens that belong to the profile graph
            composable(Screen.UserProfile.route) { backStackEntry ->
                // This is the key: get the parent graph's back stack entry
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("profile_flow")
                }
                // Create the ViewModels scoped to the parent graph
                val authViewModel: AuthViewModel = viewModel(parentEntry, factory = viewModelFactory)
                val userViewModel: UserViewModel = viewModel(parentEntry, factory = viewModelFactory)

                UserProfileRoute(
                    viewModel = userViewModel,
                    onNavigate = { route -> navController.navigate(route) },
                    onClose = { navController.popBackStack() }, // Corrected typo
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

            composable(Screen.ProfileView.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("profile_flow")
                }
                val userViewModel: UserViewModel = viewModel(parentEntry, factory = viewModelFactory)

                ProfileViewRoute(
                    viewModel = userViewModel,
                    onBackClick = { navController.popBackStack() }, // Corrected typo
                    onNavigateToEdit = { navController.navigate(Screen.EditProfile.route) }
                )
            }

            composable(Screen.EditProfile.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("profile_flow")
                }
                val userViewModel: UserViewModel = viewModel(parentEntry, factory = viewModelFactory)

                EditProfileRoute(
                    viewModel = userViewModel,
                    onBackClick = { navController.popBackStack() }, // Corrected typo
                    onSaveSuccess = { navController.popBackStack() } // Corrected typo
                )
            }

            composable(Screen.ChangePassword.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("profile_flow")
                }
                val userViewModel: UserViewModel = viewModel(parentEntry, factory = viewModelFactory)

                ChangePasswordRoute(
                    viewModel = userViewModel,
                    onBackClick = { navController.popBackStack() }, // Corrected typo
                    onChangeSuccess = { navController.popBackStack() } // Corrected typo
                )
            }
        }
    }
}
