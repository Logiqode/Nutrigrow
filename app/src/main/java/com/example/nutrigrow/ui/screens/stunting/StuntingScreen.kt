package com.example.nutrigrow.ui.screens.stunting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val genderOptions = listOf("Laki-Laki", "Perempuan")
    var selectedGender by remember { mutableStateOf(genderOptions[0]) }
    var isGenderDropdownExpanded by remember { mutableStateOf(false) }

    val isFormValid = umurBulan.isNotBlank() && tinggiBadan.isNotBlank()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Predict Baby Stunting",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Form Fields
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Age Input
            Column {
                Text(
                    text = "Umur bayi (Bulan)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = umurBulan,
                    onValueChange = { umurBulan = it.filter { char -> char.isDigit() } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE91E63),
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            // Gender Selection
            Column {
                Text(
                    text = "Jenis Kelamin",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = isGenderDropdownExpanded,
                    onExpandedChange = { isGenderDropdownExpanded = !isGenderDropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedGender,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGenderDropdownExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFE91E63),
                            unfocusedBorderColor = Color.LightGray,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
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
            }

            // Height Input
            Column {
                Text(
                    text = "Tinggi Bayi",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = tinggiBadan,
                    onValueChange = { tinggiBadan = it.filter { char -> char.isDigit() } },
                    placeholder = { Text("cm", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE91E63),
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Predict Button
        Button(
            onClick = {
                onPredictClicked(
                    umurBulan.toInt(),
                    selectedGender,
                    tinggiBadan.toInt()
                )
            },
            enabled = !uiState.isLoading && isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE91E63),
                contentColor = Color.White,
                disabledContainerColor = Color.LightGray
            )
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(
                    "PREDICT",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }

        // Error Message
        uiState.errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Prediction Result
        uiState.predictionResult?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Prediction Result",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Status Gizi: ${it.statusGizi}",
                        color = Color.Black
                    )
                    Text(
                        "Confidence: %.1f%%".format(it.confidence * 100),
                        color = Color.Black
                    )
                }
            }
        }
    }
}

