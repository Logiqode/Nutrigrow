package com.example.nutrigrow.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nutrigrow.ui.screens.bahanmakanan.BahanMakananRoute

/**
 * The "smart" stateful composable for the Home screen.
 * It will handle logic and navigation events.
 */
@Composable
fun HomeScreenRoute(
    onProfileClicked: () -> Unit
) {
    // In the future, you could get a UserViewModel here to get the user's name
    // For now, we pass the navigation event down to the screen.
    HomeScreen(
        onProfileClicked = onProfileClicked
    )
}

/**
 * The "dumb" stateless composable for the Home screen UI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProfileClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NutriGrow") },
                actions = {
                    IconButton(onClick = onProfileClicked) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User Profile"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Apply padding from the Scaffold
                .padding(horizontal = 16.dp) // Add our own horizontal padding
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Welcome Message
            Text(
                text = "Selamat Datang!", // "Welcome!"
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Jaga Gizi si Kecil Hari Ini", // "Take care of your little one's nutrition today"
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2. We are embedding the Bahan Makanan feature here
            Text(
                text = "Rekomendasi Bahan Makanan", // "Food Ingredient Recommendations"
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            BahanMakananRoute() // <-- We are reusing the component we already built!

            // You can add more sections here in the future
        }
    }
}