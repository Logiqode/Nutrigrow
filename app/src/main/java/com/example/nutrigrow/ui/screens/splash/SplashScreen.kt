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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrigrow.di.SessionManager
import com.example.nutrigrow.ui.theme.NutriGrowTheme
import com.example.nutrigrow.ui.theme.SplashBackground
import com.example.nutrigrow.ui.theme.SplashPinkDark
import com.example.nutrigrow.ui.theme.SplashPinkLight
import com.example.nutrigrow.ui.theme.SplashText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    onTimeout: () -> Unit,
    onAutoLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    
    // This effect will run once when the composable enters the screen
    LaunchedEffect(Unit) {
        delay(2000) // Wait for 2 seconds for splash display
        
        try {
            // Check for auto-login
            // First check if there's a valid session (within 2 hours)
            val authToken = sessionManager.authToken.first()
            
            if (authToken != null) {
                val sessionValid = sessionManager.isSessionValid()
                
                if (sessionValid) {
                    // Session is still valid, update last login time and go to main
                    sessionManager.updateLastLoginTime()
                    // Set the token in RetrofitClient for API calls
                    com.example.nutrigrow.api.RetrofitClient.setAuthToken(authToken)
                    onAutoLogin()
                    return@LaunchedEffect
                }
            }
            
            // If session expired, check remember me token
            val rememberMeToken = sessionManager.checkAndRefreshRememberMe()
            if (rememberMeToken != null) {
                // Remember me token is valid, restore session and go to main
                sessionManager.saveAuthToken(rememberMeToken, true)
                // Set the token in RetrofitClient for API calls
                com.example.nutrigrow.api.RetrofitClient.setAuthToken(rememberMeToken)
                onAutoLogin()
                return@LaunchedEffect
            }
            
            // No valid session or remember me, go to login
            onTimeout()
        } catch (e: Exception) {
            // If any error occurs, just go to login
            onTimeout()
        }
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

