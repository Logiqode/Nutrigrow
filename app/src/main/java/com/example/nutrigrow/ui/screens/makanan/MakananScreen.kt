package com.example.nutrigrow.ui.screens.makanan

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.models.Makanan
import com.example.nutrigrow.di.ViewModelFactory
import com.example.nutrigrow.ui.theme.ScreenBackground
import com.example.nutrigrow.ui.theme.TextDark
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.PlayCircle

@Composable
fun MakananRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel: MakananViewModel = viewModel(factory = ViewModelFactory.getInstance(context))
    val uiState by viewModel.uiState.collectAsState()

    MakananScreen(
        modifier = modifier,
        uiState = uiState,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakananScreen(
    modifier: Modifier = Modifier,
    uiState: MakananUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rekomendasi Makanan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScreenBackground,
                    titleContentColor = TextDark
                )
            )
        },
        containerColor = ScreenBackground
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
            } else if (uiState.errorMessage != null) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Error: ${uiState.errorMessage}",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else if (uiState.makananList.isEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text("Belum ada data makanan.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.makananList) { makanan ->
                        MakananCard(makanan = makanan)
                    }
                }
            }
        }
    }
}

@Composable
fun MakananCard(makanan: Makanan) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = makanan.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = FontAwesomeIcons.Solid.PlayCircle,
                    contentDescription = "Watch Tutorial",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(makanan.videoUrl))
                            context.startActivity(intent)
                        }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = makanan.deskripsi,
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Bahan:",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                text = makanan.ingredients.joinToString { it.nama },
                style = MaterialTheme.typography.bodySmall,
                color = TextDark.copy(alpha = 0.8f)
            )
        }
    }
}