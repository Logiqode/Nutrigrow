package com.example.nutrigrow.ui.screens.tracking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.nutrigrow.ui.screens.calendar.CalendarViewModel
import com.example.nutrigrow.di.ViewModelFactory
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen(
    onNavigateToStunting: () -> Unit = {},
    onNavigateToCalendarNotes: (LocalDate) -> Unit = {},
    onAddChild: () -> Unit = {},
    viewModelFactory: ViewModelFactory
) {
    val calendarViewModel: CalendarViewModel = viewModel(factory = viewModelFactory)
    val uiState by calendarViewModel.uiState.collectAsState()
    
    var selectedYear by remember { mutableStateOf(uiState.selectedYear) }
    var selectedMonth by remember { mutableStateOf(uiState.selectedMonth) }
    
    // Update local state when ViewModel state changes
    LaunchedEffect(uiState.selectedYear, uiState.selectedMonth) {
        selectedYear = uiState.selectedYear
        selectedMonth = uiState.selectedMonth
    }
    
    val yearOptions = (2020..2030).toList()
    val monthOptions = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    
    var isYearDropdownExpanded by remember { mutableStateOf(false) }
    var isMonthDropdownExpanded by remember { mutableStateOf(false) }
    
    val currentYearMonth = YearMonth.of(selectedYear, selectedMonth)
    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstDayOfMonth = currentYearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Convert to 0-6 where 0 is Sunday
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Predict Stunting Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToStunting() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE91E63).copy(alpha = 0.1f)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PREDICT NOW",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFE91E63)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Tracking Calendar Section
        Text(
            text = "Tracking Calendar",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Year and Month Selectors
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Year Dropdown
            ExposedDropdownMenuBox(
                expanded = isYearDropdownExpanded,
                onExpandedChange = { isYearDropdownExpanded = !isYearDropdownExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedYear.toString(),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isYearDropdownExpanded)
                    },
                    modifier = Modifier.menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE91E63),
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                ExposedDropdownMenu(
                    expanded = isYearDropdownExpanded,
                    onDismissRequest = { isYearDropdownExpanded = false }
                ) {
                    yearOptions.forEach { year ->
                        DropdownMenuItem(
                            text = { Text(year.toString()) },
                            onClick = {
                                selectedYear = year
                                calendarViewModel.changeMonth(year, selectedMonth)
                                isYearDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            
            // Month Dropdown
            ExposedDropdownMenuBox(
                expanded = isMonthDropdownExpanded,
                onExpandedChange = { isMonthDropdownExpanded = !isMonthDropdownExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = monthOptions[selectedMonth - 1],
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isMonthDropdownExpanded)
                    },
                    modifier = Modifier.menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE91E63),
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                ExposedDropdownMenu(
                    expanded = isMonthDropdownExpanded,
                    onDismissRequest = { isMonthDropdownExpanded = false }
                ) {
                    monthOptions.forEachIndexed { index, month ->
                        DropdownMenuItem(
                            text = { Text(month) },
                            onClick = {
                                selectedMonth = index + 1
                                calendarViewModel.changeMonth(selectedYear, index + 1)
                                isMonthDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Calendar Grid
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Days of week header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                        Text(
                            text = day,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Calendar days
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.height(240.dp)
                ) {
                    // Empty cells for days before the first day of the month
                    items(firstDayOfWeek) {
                        Box(modifier = Modifier.size(32.dp))
                    }
                    
                    // Days of the month
                    items((1..daysInMonth).toList()) { day ->
                        val hasNote = uiState.calendarNotes.any { note ->
                            try {
                                val noteDate = LocalDate.parse(note.date)
                                noteDate.dayOfMonth == day && 
                                noteDate.monthValue == selectedMonth && 
                                noteDate.year == selectedYear
                            } catch (e: Exception) {
                                false
                            }
                        }
                        
                        CalendarDayItem(
                            day = day,
                            hasNote = hasNote,
                            onClick = {
                                val selectedDate = LocalDate.of(selectedYear, selectedMonth, day)
                                onNavigateToCalendarNotes(selectedDate)
                            }
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Add Child Button
        Button(
            onClick = onAddChild,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE91E63),
                contentColor = Color.White
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Child"
                )
                Text(
                    "Add New Child",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun CalendarDayItem(
    day: Int,
    hasNote: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .background(
                if (hasNote) Color(0xFFE91E63).copy(alpha = 0.2f) 
                else Color.Transparent
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = if (hasNote) Color(0xFFE91E63) else Color.Black,
            fontWeight = if (hasNote) FontWeight.Bold else FontWeight.Normal
        )
    }
}

