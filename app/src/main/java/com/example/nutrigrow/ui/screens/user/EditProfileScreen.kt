package com.example.nutrigrow.ui.screens.user

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.di.ViewModelFactory
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. The Stateful "Route" Composable
@Composable
fun EditProfileRoute(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: UserViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // When the update is successful, we navigate back.
    LaunchedEffect(uiState.isUpdateSuccess) {
        if (uiState.isUpdateSuccess) {
            viewModel.clearUpdateSuccess() // Reset the flag
            onSaveSuccess()
        }
    }

    // Pass state down and events up
    EditProfileScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onSave = { name, email ->
            // FIX: Call the new, more specific function. The gender parameter is gone.
            viewModel.updateProfileDetails(name, email)
        }
    )
}


// 2. Your Original Stateless UI (with one small change)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    // It now receives the whole uiState object
    uiState: UserUiState,
    onBackClick: () -> Unit,
    onSave: (String, String) -> Unit
) {
    // Use the user from the state, or show empty fields if null
    val user = uiState.user ?: return // Or show a loading/error screen

    var name by remember(user.id) { mutableStateOf(user.name) }
    var email by remember(user.id) { mutableStateOf(user.email) }

    val isSaveEnabled = !uiState.isLoading && !name.isNullOrBlank() && !email.isNullOrBlank()


    // The Save button calls the onSave lambda
    Button(
        onClick = {
            // At this point, we know name and email are not null, so we can assert it with !!
            onSave(name!!, email!!)
        },
        // The button is only enabled if the fields are valid AND not loading
        enabled = isSaveEnabled,
        // ... other button properties
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
        } else {
            Text(text = "Save", color = Color.White, fontSize = 16.sp)
        }
    }
}