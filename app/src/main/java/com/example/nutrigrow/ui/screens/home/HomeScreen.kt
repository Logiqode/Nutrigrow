package com.example.nutrigrow.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrigrow.ui.screens.bahanmakanan.BahanMakananRoute
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * The "smart" stateful composable for the Home screen.
 * It will handle logic and navigation events.
 */
@Composable
fun HomeScreenRoute(
    onProfileClicked: () -> Unit,
    onNavigateToStunting: () -> Unit
) {
    HomeScreen(
        onProfileClicked = onProfileClicked,
        onNavigateToStunting = onNavigateToStunting
    )
}

/**
 * The "dumb" stateless composable for the Home screen UI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProfileClicked: () -> Unit,
    onNavigateToStunting: () -> Unit
) {
    val scrollState = rememberScrollState()
    
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
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Daily Cards Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Daily Exercises Card
                DailyExercisesCard(
                    modifier = Modifier.weight(1f)
                )
                
                // Your Daily Mood Card
                DailyMoodCard(
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search Ingredients Section
            BahanMakananRoute()

            Spacer(modifier = Modifier.height(24.dp))

            // Calendar Section
            CalendarSection()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DailyExercisesCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFB3E5FC) // Light blue
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Daily\nExercises",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0277BD),
                lineHeight = 20.sp
            )
            
            Column {
                Text(
                    text = "You have walked\nfor 30Mins",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF0277BD),
                    lineHeight = 14.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Tue, 3 Feb",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF0277BD).copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun DailyMoodCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF9C4) // Light yellow
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Your Daily\nMood",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF57F17),
                lineHeight = 20.sp
            )
            
            Column {
                Text(
                    text = "You logged a\nHappy Mood",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFF57F17),
                    lineHeight = 14.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Yesterday",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFF57F17).copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun CalendarSection() {
    val currentDate = LocalDate.now()
    val currentMonth = currentDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
    
    // Generate sample calendar data
    val calendarItems = listOf(
        CalendarItem(5, "Sun", "Vegetables", Color(0xFF4CAF50)),
        CalendarItem(6, "Mon", "Banana", Color(0xFFFF9800)),
        CalendarItem(7, "Tue", "Tea\nMusic", Color(0xFF2196F3)),
        CalendarItem(8, "Wed", "Must", Color(0xFF9C27B0))
    )
    
    Column {
        // Month selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentMonth,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            
            // Calendar icon button
            IconButton(
                onClick = { /* Handle calendar click */ },
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.1f),
                        RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    text = "📅",
                    fontSize = 16.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Calendar items
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(calendarItems) { item ->
                CalendarItemCard(item = item)
            }
        }
    }
}

data class CalendarItem(
    val day: Int,
    val dayName: String,
    val activity: String,
    val color: Color
)

@Composable
fun CalendarItemCard(
    item: CalendarItem
) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = item.color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.dayName,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontSize = 10.sp
            )
            
            Text(
                text = item.day.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 24.sp
            )
            
            // Activity icon/indicator
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(item.color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (item.activity) {
                        "Vegetables" -> "🥬"
                        "Banana" -> "🍌"
                        "Tea\nMusic" -> "🎵"
                        "Must" -> "✨"
                        else -> "📝"
                    },
                    fontSize = 12.sp
                )
            }
            
            Text(
                text = item.activity,
                style = MaterialTheme.typography.bodySmall,
                color = item.color,
                fontSize = 8.sp,
                textAlign = TextAlign.Center,
                lineHeight = 10.sp,
                maxLines = 2
            )
        }
    }
}

