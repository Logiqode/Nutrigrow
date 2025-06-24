package com.example.nutrigrow.ui.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nutrigrow.di.ViewModelFactory
import com.example.nutrigrow.ui.components.BottomNavigationBar
import com.example.nutrigrow.ui.screens.home.HomeScreenRoute
import com.example.nutrigrow.ui.screens.food.FoodRoute
import com.example.nutrigrow.ui.screens.stunting.StuntingRoute
import com.example.nutrigrow.ui.screens.tracking.TrackingScreen
import com.example.nutrigrow.ui.screens.calendar.CalendarNotesScreen
import com.example.nutrigrow.ui.screens.child.AddChildScreen
import com.example.nutrigrow.ui.screens.user.UserProfileRoute
import com.example.nutrigrow.ui.screens.user.UserViewModel
import java.time.LocalDate

@Composable
fun MainScreen(
    onLogout: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Only show bottom navigation on main screens
            if (shouldShowBottomNav(currentRoute)) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Pop up to the start destination to avoid building up a large stack
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        MainNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            onLogout = onLogout
        )
    }
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModelFactory = ViewModelFactory.getInstance(context)
    
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreenRoute(
                onProfileClicked = {
                    navController.navigate("account")
                },
                onNavigateToStunting = {
                    navController.navigate("stunting")
                }
            )
        }
        
        composable("feed") {
            FoodRoute()
        }
        
        composable("scan") {
            StuntingRoute()
        }
        
        composable("track") {
            TrackingScreen(
                onNavigateToStunting = {
                    navController.navigate("scan")
                },
                onNavigateToCalendarNotes = { selectedDate ->
                    navController.navigate("calendar_notes/${selectedDate}")
                },
                onAddChild = {
                    navController.navigate("add_child")
                },
                viewModelFactory = viewModelFactory
            )
        }
        
        composable("account") {
            val userViewModel: UserViewModel = viewModel(factory = viewModelFactory)
            UserProfileRoute(
                viewModel = userViewModel,
                onNavigate = { route -> navController.navigate(route) },
                onClose = { navController.popBackStack() },
                onLogout = onLogout
            )
        }
        
        composable("stunting") {
            StuntingRoute()
        }
        
        composable("calendar_notes/{selectedDate}") { backStackEntry ->
            val selectedDateString = backStackEntry.arguments?.getString("selectedDate")
            val selectedDate = selectedDateString?.let { LocalDate.parse(it) } ?: LocalDate.now()
            
            CalendarNotesScreen(
                selectedDate = selectedDate,
                onBackClick = {
                    navController.popBackStack()
                },
                onAddChild = {
                    navController.navigate("add_child")
                },
                viewModelFactory = viewModelFactory
            )
        }
        
        composable("add_child") {
            AddChildScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveChild = { childData ->
                    navController.popBackStack()
                }
            )
        }
    }
}

private fun shouldShowBottomNav(currentRoute: String?): Boolean {
    return when (currentRoute) {
        "home", "feed", "scan", "track", "account" -> true
        else -> false
    }
}

