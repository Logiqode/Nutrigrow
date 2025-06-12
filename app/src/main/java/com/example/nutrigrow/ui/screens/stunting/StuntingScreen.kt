package com.example.nutrigrow.ui.screens.stunting

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.di.ViewModelFactory

@Composable
fun StuntingRoute(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val viewModel: StuntingViewModel = viewModel(factory = ViewModelFactory.getInstance(context))
    val uiState by viewModel.uiState.collectAsState()

    StuntingScreen(
        modifier = modifier,
        uiState = uiState,
        onPredictClicked = { umurBulan, jenisKelamin, tinggiBadan ->
            viewModel.predictStunting(umurBulan, jenisKelamin, tinggiBadan)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StuntingScreen(
    modifier: Modifier = Modifier,
    uiState: StuntingUiState,
    onPredictClicked: (Int, String, Int) -> Unit
) {
    var umurBulan by remember { mutableStateOf("") }
    var tinggiBadan by remember { mutableStateOf("") }

    val genderOptions = listOf("Laki-laki", "Perempuan")
    var selectedGender by remember { mutableStateOf(genderOptions[0]) }
    var isGenderDropdownExpanded by remember { mutableStateOf(false) }

    val isFormValid = umurBulan.isNotBlank() && tinggiBadan.isNotBlank()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Stunting Prediction", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = umurBulan,
            onValueChange = { umurBulan = it.filter { char -> char.isDigit() } },
            label = { Text("Umur (bulan)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = isGenderDropdownExpanded,
            onExpandedChange = { isGenderDropdownExpanded = !isGenderDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedGender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Jenis Kelamin") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGenderDropdownExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = isGenderDropdownExpanded,
                onDismissRequest = { isGenderDropdownExpanded = false }
            ) {
                genderOptions.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            selectedGender = gender
                            isGenderDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = tinggiBadan,
            onValueChange = { tinggiBadan = it.filter { char -> char.isDigit() } },
            label = { Text("Tinggi Badan (cm)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onPredictClicked(
                    umurBulan.toInt(),
                    selectedGender,
                    tinggiBadan.toInt()
                )
            },
            enabled = !uiState.isLoading && isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Predict")
            }
        }

        uiState.errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        uiState.predictionResult?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Prediction Result",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Status Gizi: ${it.statusGizi}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        "Confidence: %.1f%%".format(it.confidence * 100),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}