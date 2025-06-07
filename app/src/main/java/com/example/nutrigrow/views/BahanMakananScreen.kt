package com.example.nutrigrow.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.viewmodel.BahanMakananViewModel
import com.example.nutrigrow.models.BahanMakanan

@Composable
fun BahanMakananScreen() {
    val viewModel: BahanMakananViewModel = viewModel()
    val bahanMakanan by viewModel.bahanMakananList
    val isLoading by viewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { viewModel.loadBahanMakanan() },
            enabled = !isLoading
        ) {
            Text("Muat Bahan Makanan")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> CircularProgressIndicator()
            bahanMakanan.isEmpty() -> Text("Belum ada data bahan makanan")
            else -> {
                LazyColumn {
                    items(bahanMakanan) { bahan ->
                        BahanMakananCard(bahan = bahan)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
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
            Text(
                text = bahan.deskripsi,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}