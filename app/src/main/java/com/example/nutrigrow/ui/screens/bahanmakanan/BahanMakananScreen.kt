package com.example.nutrigrow.ui.screens.bahanmakanan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var searchText by remember { mutableStateOf("") }
    
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Search Ingredients Section
        Text(
            text = "Search Ingredients",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Turmeric", color = Color.Gray) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.LightGray
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Handle the UI based on the single uiState object
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: ${uiState.errorMessage}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRefreshClicked) {
                        Text("Retry")
                    }
                }
            }
        } else if (uiState.bahanMakananList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Belum ada data bahan makanan.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRefreshClicked) {
                        Text("Muat Bahan Makanan")
                    }
                }
            }
        } else {
            // Ingredients Carousel
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(uiState.bahanMakananList) { bahan ->
                    BahanMakananCarouselCard(bahan = bahan)
                }
            }
        }
    }
}

@Composable
fun BahanMakananCarouselCard(bahan: BahanMakanan) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Placeholder for ingredient icon/image
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                // You can replace this with actual ingredient images
                Text(
                    text = bahan.nama.take(2).uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = bahan.nama,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// --- PREVIEWS to visualize our component in different states ---

@Preview(showBackground = true)
@Composable
fun BahanMakananScreenPreview() {
    val sampleList = listOf(
        BahanMakanan(id = "1", nama = "Ginger", deskripsi = "Sayuran hijau kaya zat besi."),
        BahanMakanan(id = "2", nama = "Carrot", deskripsi = "Sumber protein hewani yang baik."),
        BahanMakanan(id = "3", nama = "Celery", deskripsi = "Sayuran segar."),
        BahanMakanan(id = "4", nama = "Onion", deskripsi = "Bumbu dapur."),
        BahanMakanan(id = "5", nama = "Nutmeg", deskripsi = "Rempah-rempah.")
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

