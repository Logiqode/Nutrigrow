package com.example.nutrigrow.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrigrow.ui.theme.ButtonPink
import com.example.nutrigrow.ui.theme.FabPink
import com.example.nutrigrow.ui.theme.ScreenBackground
import com.example.nutrigrow.ui.theme.TextDark

/**
 * The "smart" stateful composable for the Home screen.
 * It will handle logic and navigation events.
 */
@Composable
fun HomeScreenRoute(
    onProfileClicked: () -> Unit,
    onNavigateToStunting: () -> Unit,
    onNavigateToFoodRecommendation: () -> Unit
) {
    // For now, we pass the navigation event down to the screen.
    HomeScreen(
        onProfileClicked = onProfileClicked,
        onNavigateToStunting = onNavigateToStunting,
        onNavigateToFoodRecommendation = onNavigateToFoodRecommendation
    )
}

/**
 * The "dumb" stateless composable for the Home screen UI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProfileClicked: () -> Unit,
    onNavigateToStunting: () -> Unit,
    onNavigateToFoodRecommendation: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NutriGrow", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onProfileClicked) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User Profile",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScreenBackground
                )
            )
        },
        containerColor = ScreenBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Welcome Message
            Text(
                text = "Selamat Datang!",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = TextDark)
            )
            Text(
                text = "Jaga Gizi si Kecil Hari Ini",
                style = MaterialTheme.typography.bodyLarge.copy(color = TextDark.copy(alpha = 0.7f))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main feature cards
            FeatureCard(
                title = "Cek Status Gizi",
                description = "Prediksi stunting pada anak",
                icon = Icons.Default.BarChart,
                onClick = onNavigateToStunting
            )
            Spacer(modifier = Modifier.height(16.dp))
            FeatureCard(
                title = "Rekomendasi Makanan",
                description = "Cari rekomendasi makanan bergizi",
                icon = Icons.Default.RestaurantMenu,
                onClick = onNavigateToFoodRecommendation
            )
        }
    }
}


@Composable
fun FeatureCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(ButtonPink),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = FabPink,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                Text(text = description, fontSize = 14.sp, color = TextDark.copy(alpha = 0.7f))
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Go to $title",
                tint = TextDark.copy(alpha = 0.7f)
            )
        }
    }
}