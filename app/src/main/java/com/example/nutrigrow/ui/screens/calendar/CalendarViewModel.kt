package com.example.nutrigrow.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrigrow.api.ApiService
import com.example.nutrigrow.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class CalendarUiState(
    val calendarNotes: List<CalendarNote> = emptyList(),
    val children: List<Child> = emptyList(),
    val selectedMonth: Int = LocalDate.now().monthValue,
    val selectedYear: Int = LocalDate.now().year,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class CalendarViewModel(private val apiService: ApiService) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        loadCalendarData()
    }

    fun loadCalendarData(year: Int? = null, month: Int? = null) {
        val targetYear = year ?: _uiState.value.selectedYear
        val targetMonth = month ?: _uiState.value.selectedMonth
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = apiService.getStuntingCalendar(targetYear, targetMonth)
                if (response.isSuccessful) {
                    val stuntingData = response.body()?.data ?: emptyList()
                    
                    // Convert StuntingResponse to CalendarNote and extract children
                    val calendarNotes = stuntingData.map { stunting ->
                        CalendarNote(
                            id = stunting.id,
                            childId = stunting.userId.toString(),
                            childName = stunting.user?.name ?: "Unknown Child",
                            jenisKelamin = stunting.jenisKelamin,
                            date = formatDate(stunting.createdAt),
                            height = stunting.tinggiBadan,
                            notes = stunting.catatanStunting,
                            hasilPrediksi = stunting.hasilPrediksi
                        )
                    }
                    
                    // Extract unique children from the data
                    val children = stuntingData.mapNotNull { stunting ->
                        stunting.user?.let { user ->
                            Child(
                                id = user.id,
                                name = user.name ?: "Unknown",
                                jenisKelamin = stunting.jenisKelamin
                            )
                        }
                    }.distinctBy { it.id }
                    
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            calendarNotes = calendarNotes,
                            children = children,
                            selectedYear = targetYear,
                            selectedMonth = targetMonth
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to load calendar data: ${response.code()} ${response.message()}. Details: ${errorBody ?: "No details"}"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "An unknown error occurred"
                    )
                }
            }
        }
    }

    fun createCalendarNote(
        userId: String,
        jenisKelamin: String,
        tinggiBadan: Double,
        catatanStunting: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val request = CalendarNoteCreateRequest(
                    userId = userId,
                    jenisKelamin = jenisKelamin,
                    tinggiBadan = tinggiBadan,
                    catatanStunting = catatanStunting
                )
                
                val response = apiService.createStuntingRecord(request)
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Calendar note saved successfully!"
                        )
                    }
                    // Reload calendar data to show the new note
                    loadCalendarData()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to save calendar note: ${response.code()} ${response.message()}. Details: ${errorBody ?: "No details"}"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "An unknown error occurred"
                    )
                }
            }
        }
    }

    fun updateCalendarNote(
        noteId: String,
        jenisKelamin: String?,
        tinggiBadan: Double?,
        catatanStunting: String?
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val request = CalendarNoteUpdateRequest(
                    jenisKelamin = jenisKelamin,
                    tinggiBadan = tinggiBadan,
                    catatanStunting = catatanStunting
                )
                
                val response = apiService.updateStuntingRecord(noteId, request)
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Calendar note updated successfully!"
                        )
                    }
                    // Reload calendar data to show the updated note
                    loadCalendarData()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to update calendar note: ${response.code()} ${response.message()}. Details: ${errorBody ?: "No details"}"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "An unknown error occurred"
                    )
                }
            }
        }
    }

    fun deleteCalendarNote(noteId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = apiService.deleteStuntingRecord(noteId)
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Calendar note deleted successfully!"
                        )
                    }
                    // Reload calendar data to remove the deleted note
                    loadCalendarData()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to delete calendar note: ${response.code()} ${response.message()}. Details: ${errorBody ?: "No details"}"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "An unknown error occurred"
                    )
                }
            }
        }
    }

    fun changeMonth(year: Int, month: Int) {
        loadCalendarData(year, month)
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    private fun formatDate(dateString: String?): String {
        return try {
            // Parse ISO 8601 date format from backend (e.g., "2025-06-24T10:30:00Z")
            if (dateString != null) {
                // Extract just the date part (YYYY-MM-DD) from the ISO string
                val datePart = dateString.split("T")[0]
                datePart
            } else {
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            }
        } catch (e: Exception) {
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }
}

