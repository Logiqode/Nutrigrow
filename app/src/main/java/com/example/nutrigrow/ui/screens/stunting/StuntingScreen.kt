package com.example.nutrigrow.ui.screens.stunting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.di.ViewModelFactory
import com.example.nutrigrow.ui.theme.NutriGrowTheme
import com.example.nutrigrow.ui.theme.PrimaryPink
import com.example.nutrigrow.ui.theme.ScreenBackground
import com.example.nutrigrow.ui.theme.SplashText
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun StuntingRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: StuntingViewModel = viewModel(factory = ViewModelFactory.getInstance(context))
    val uiState by viewModel.uiState.collectAsState()

    StuntingScreen(
        modifier = modifier,
        uiState = uiState,
        onPredictClicked = { umurBulan, jenisKelamin, tinggiBadan ->
            viewModel.predictStunting(umurBulan, jenisKelamin, tinggiBadan)
        },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StuntingScreen(
    modifier: Modifier = Modifier,
    uiState: StuntingUiState,
    onPredictClicked: (Int, String, Int) -> Unit,
    onBackClick: () -> Unit
) {
    var umurBulan by remember { mutableStateOf("") }
    var tinggiBadan by remember { mutableStateOf("") }

    val genderOptions = listOf("Laki-laki", "Perempuan")
    var selectedGender by remember { mutableStateOf(genderOptions[0]) }
    var isGenderDropdownExpanded by remember { mutableStateOf(false) }

    val isFormValid = umurBulan.isNotBlank() && tinggiBadan.isNotBlank()

    val currentDate = LocalDate.now()
    val dayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale("id", "ID"))
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id", "ID"))
    val dayName = currentDate.format(dayFormatter).uppercase()
    val formattedDate = currentDate.format(dateFormatter)

    Scaffold(
        containerColor = ScreenBackground
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Predict Baby Stunting",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text(
                text = dayName,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = SplashText
            )
            Text(
                text = formattedDate,
                fontSize = 18.sp,
                color = SplashText.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            StuntingInputField(label = "Umur bayi (Bulan)", value = umurBulan, onValueChange = { umurBulan = it.filter { char -> char.isDigit() } })
            Spacer(modifier = Modifier.height(16.dp))

            StuntingDropdownField(
                label = "Jenis Kelamin",
                selectedValue = selectedGender,
                options = genderOptions,
                isExpanded = isGenderDropdownExpanded,
                onExpandedChange = { isGenderDropdownExpanded = it },
                onValueChange = { selectedGender = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            StuntingInputField(label = "Tinggi Bayi", value = tinggiBadan, onValueChange = { tinggiBadan = it.filter { char -> char.isDigit() } }, trailingText = "cm")

            Spacer(modifier = Modifier.height(32.dp))

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
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("PREDICT", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (uiState.errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.onError, modifier = Modifier.padding(16.dp))
                }
            }

            if (uiState.predictionResult != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Prediction Result", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Status Gizi: ${uiState.predictionResult.statusGizi}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("Confidence: %.1f%%".format(uiState.predictionResult.confidence * 100), color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StuntingInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    height: Dp = Dp.Unspecified,
    trailingText: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = SplashText.copy(alpha = 0.8f), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            singleLine = singleLine,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryPink,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.4f),
                cursorColor = PrimaryPink,
                focusedTextColor = SplashText,
                unfocusedTextColor = SplashText,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            trailingIcon = {
                if (trailingText != null) {
                    Text(trailingText, color = Color.Gray, modifier = Modifier.padding(end = 8.dp))
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StuntingDropdownField(
    label: String,
    selectedValue: String,
    options: List<String>,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = SplashText.copy(alpha = 0.8f), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = onExpandedChange
        ) {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPink,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.4f),
                    focusedTextColor = SplashText,
                    unfocusedTextColor = SplashText,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun StuntingScreenPreview() {
    NutriGrowTheme {
        StuntingScreen(
            uiState = StuntingUiState(),
            onPredictClicked = { _, _, _ -> },
            onBackClick = {}
        )
    }
}