package com.example.nutrigrow.ui.screens.bahanmakanan // or your feature package

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrigrow.api.ApiService
import com.example.nutrigrow.models.BahanMakanan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 1. A dedicated UI state class. This bundles all related state together.
data class BahanMakananUiState(
    val bahanMakananList: List<BahanMakanan> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// 2. Fix the constructor to accept the ApiService dependency.
class BahanMakananViewModel(
    private val apiService: ApiService
) : ViewModel() {

    // 3. Use a single StateFlow for the UI state.
    private val _uiState = MutableStateFlow(BahanMakananUiState())
    val uiState: StateFlow<BahanMakananUiState> = _uiState.asStateFlow()

    // The 'init' block runs when the ViewModel is first created.
    // This is a good place to load initial data.
    init {
        loadBahanMakanan()
    }

    /**
     * Fetches the list of food items from the API and updates the UI state.
     */
    fun loadBahanMakanan() {
        // Prevent multiple simultaneous loads.
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            // Set loading state and clear previous errors.
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val response = apiService.getBahanMakanan()

                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()
                    // On success, update the list and stop loading.
                    _uiState.update {
                        it.copy(isLoading = false, bahanMakananList = data)
                    }
                } else {
                    // On failure, set an error message for the UI to display.
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Failed to load data: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                // On exception (e.g., no internet), set an error message.
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "An unknown error occurred.")
                }
            }
        }
    }

    /**
     * Clears the error message from the UI state.
     * This can be called by the UI after an error has been shown.
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}