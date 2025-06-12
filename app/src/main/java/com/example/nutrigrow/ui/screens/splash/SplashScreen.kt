package com.example.nutrigrow.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrigrow.ui.theme.NutriGrowTheme
import com.example.nutrigrow.ui.theme.SplashBackground
import com.example.nutrigrow.ui.theme.SplashPinkDark
import com.example.nutrigrow.ui.theme.SplashPinkLight
import com.example.nutrigrow.ui.theme.SplashText
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    // This effect will run once when the composable enters the screen
    LaunchedEffect(Unit) {
        delay(2000) // Wait for 2 seconds
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground),
        contentAlignment = Alignment.Center
    ) {
        // Logo
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Outer circle
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(SplashPinkLight)
            )
            // Inner circle
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .clip(CircleShape)
                    .background(SplashPinkDark)
            )
            // Text
            Text(
                text = "NutriGrow",
                color = SplashText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Copyright text at the bottom
        Text(
            text = "Copyright ©2025",
            color = SplashText.copy(alpha = 0.8f),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    NutriGrowTheme {
        SplashScreen(onTimeout = {})
    }
}