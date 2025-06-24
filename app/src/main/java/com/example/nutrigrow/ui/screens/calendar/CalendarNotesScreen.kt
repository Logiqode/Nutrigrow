package com.example.nutrigrow.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.di.ViewModelFactory
import com.example.nutrigrow.models.Child
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarNotesScreen(
    selectedDate: LocalDate,
    onBackClick: () -> Unit = {},
    onAddChild: () -> Unit = {},
    viewModelFactory: ViewModelFactory
) {
    val calendarViewModel: CalendarViewModel = viewModel(factory = viewModelFactory)
    val uiState by calendarViewModel.uiState.collectAsState()
    
    var selectedChildIndex by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    
    // Get existing note for selected date and child
    val existingNote = remember(selectedDate, selectedChildIndex, uiState.calendarNotes) {
        if (uiState.children.isNotEmpty() && selectedChildIndex < uiState.children.size) {
            val selectedChild = uiState.children[selectedChildIndex]
            uiState.calendarNotes.find { note ->
                note.childId == selectedChild.id && 
                try {
                    val noteDate = LocalDate.parse(note.date)
                    noteDate.isEqual(selectedDate)
                } catch (e: Exception) {
                    false
                }
            }
        } else null
    }
    
    // Update form fields when existing note is found
    LaunchedEffect(existingNote) {
        existingNote?.let { note ->
            height = note.height.toString()
            notes = note.notes
        } ?: run {
            height = ""
            notes = ""
        }
    }
    
    // Show loading indicator
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFE91E63))
        }
        return
    }
    
    // Show error message
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // You can show a snackbar or toast here
        }
    }
    
    // Show success message
    uiState.successMessage?.let { success ->
        LaunchedEffect(success) {
            // You can show a snackbar or toast here
            calendarViewModel.clearMessages()
        }
    }
    
    val currentChild = if (uiState.children.isNotEmpty() && selectedChildIndex < uiState.children.size) {
        uiState.children[selectedChildIndex]
    } else null
    
    // Format date
    val dayName = selectedDate.format(DateTimeFormatter.ofPattern("EEEE", Locale("id", "ID")))
    val dateFormatted = selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id", "ID")))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendar Notes") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            // Date Display
            Text(
                text = "$dayName\n$dateFormatted",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
            
            // Child Carousel
            if (uiState.children.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(uiState.children.indices.toList()) { index ->
                            ChildCarouselItem(
                                child = uiState.children[index],
                                isSelected = index == selectedChildIndex,
                                onClick = { 
                                    selectedChildIndex = index
                                    // Reset form when switching children
                                    height = ""
                                    notes = ""
                                }
                            )
                        }
                        
                        // Add child button
                        item {
                            Card(
                                modifier = Modifier
                                    .size(60.dp),
                                shape = CircleShape,
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE91E63).copy(alpha = 0.1f)
                                ),
                                onClick = onAddChild
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Add Child",
                                        tint = Color(0xFFE91E63)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // No children message
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE91E63).copy(alpha = 0.1f)
                    ),
                    onClick = onAddChild
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add your first child to start tracking",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFFE91E63),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Form Fields (only show if there are children)
            if (uiState.children.isNotEmpty() && currentChild != null) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Height Input
                    Column {
                        Text(
                            text = "Tinggi Anak (cm)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = height,
                            onValueChange = { height = it.filter { char -> char.isDigit() || char == '.' } },
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
                    
                    // Notes Input
                    Column {
                        Text(
                            text = "Catatan",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFE91E63),
                                unfocusedBorderColor = Color.LightGray,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            maxLines = 5
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Save Button
                Button(
                    onClick = {
                        currentChild?.let { child ->
                            val heightValue = height.toDoubleOrNull()
                            if (heightValue != null && heightValue > 0) {
                                if (existingNote != null) {
                                    // Update existing note
                                    calendarViewModel.updateCalendarNote(
                                        noteId = existingNote.id.toString(),
                                        jenisKelamin = child.jenisKelamin,
                                        tinggiBadan = heightValue,
                                        catatanStunting = notes
                                    )
                                } else {
                                    // Create new note
                                    calendarViewModel.createCalendarNote(
                                        userId = child.id,
                                        jenisKelamin = child.jenisKelamin,
                                        tinggiBadan = heightValue,
                                        catatanStunting = notes
                                    )
                                }
                            }
                        }
                    },
                    enabled = height.toDoubleOrNull() != null && height.toDoubleOrNull()!! > 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE91E63),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        if (existingNote != null) "UPDATE" else "SAVE",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ChildCarouselItem(
    child: Child,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.size(60.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE91E63) else Color.White
        ),
        border = if (!isSelected) CardDefaults.outlinedCardBorder() else null,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = child.name.take(2).uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color.Black,
                fontSize = 12.sp
            )
        }
    }
}

