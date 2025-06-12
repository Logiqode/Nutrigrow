package com.example.nutrigrow.ui.screens.bahanmakanan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Correct import for LazyColumn items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.models.BahanMakanan
import com.example.nutrigrow.ui.screens.bahanmakanan.BahanMakananUiState
import com.example.nutrigrow.ui.screens.bahanmakanan.BahanMakananViewModel
import com.example.nutrigrow.di.ViewModelFactory
import androidx.compose.ui.platform.LocalContext

/**
 * The "smart" or stateful composable.
 * It handles the ViewModel, state collection, and passes events.
 */
@Composable
fun BahanMakananRoute(
    modifier: Modifier = Modifier,
) {
    // 1. Get the context from the composable environment.
    val context = LocalContext.current
    // 2. Create the ViewModel using the factory, passing the context.
    val viewModel: BahanMakananViewModel = viewModel(factory = ViewModelFactory.getInstance(context))

    // Collect the single UI state object from the ViewModel's StateFlow
    val uiState by viewModel.uiState.collectAsState()

    // Pass the state down to the stateless screen and pass events up
    BahanMakananScreen(
        modifier = modifier,
        uiState = uiState,
        onRefreshClicked = { viewModel.loadBahanMakanan() }
    )
}

/**
 * The "dumb" or stateless composable.
 * It only knows how to display the UI based on the state it's given.
 * It is highly reusable and easy to preview.
 */
@Composable
fun BahanMakananScreen(
    modifier: Modifier = Modifier,
    uiState: BahanMakananUiState,
    onRefreshClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onRefreshClicked,
            enabled = !uiState.isLoading // Use the isLoading property from uiState
        ) {
            Text("Muat Bahan Makanan")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Handle the UI based on the single uiState object
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.errorMessage != null) {
            // New: We can now display errors received from the ViewModel
            Text("Error: ${uiState.errorMessage}")
        } else if (uiState.bahanMakananList.isEmpty()) {
            Text("Belum ada data bahan makanan.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp) // A cleaner way to add space
            ) {
                items(uiState.bahanMakananList) { bahan ->
                    BahanMakananCard(bahan = bahan)
                }
            }
        }
    }
}

@Composable
fun BahanMakananCard(bahan: BahanMakanan) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = bahan.nama,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            bahan.deskripsi?.let { // Good practice to handle nullable descriptions
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// --- PREVIEWS to visualize our component in different states ---

@Preview(showBackground = true)
@Composable
fun BahanMakananScreenPreview() {
    val sampleList = listOf(
        BahanMakanan(id = "1", nama = "Bayam", deskripsi = "Sayuran hijau kaya zat besi."),
        BahanMakanan(id = "2", nama = "Telur", deskripsi = "Sumber protein hewani yang baik.")
    )
    BahanMakananScreen(
        uiState = BahanMakananUiState(bahanMakananList = sampleList),
        onRefreshClicked = {}
    )
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun BahanMakananScreenLoadingPreview() {
    BahanMakananScreen(
        uiState = BahanMakananUiState(isLoading = true),
        onRefreshClicked = {}
    )
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun BahanMakananScreenErrorPreview() {
    BahanMakananScreen(
        uiState = BahanMakananUiState(errorMessage = "Gagal memuat. Periksa koneksi internet Anda."),
        onRefreshClicked = {}
    )
}