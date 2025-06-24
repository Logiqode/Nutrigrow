package com.example.nutrigrow.ui.screens.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrigrow.api.ApiService
import com.example.nutrigrow.models.BahanMakanan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// UI state class for Food screen
data class FoodUiState(
    val foodList: List<BahanMakanan> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class FoodViewModel(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodUiState())
    val uiState: StateFlow<FoodUiState> = _uiState.asStateFlow()

    // Load food data when ViewModel is created
    init {
        loadFoodData()
    }

    /**
     * Fetches the list of makanan from the API and updates the UI state.
     */
    fun loadFoodData() {
        // Prevent multiple simultaneous loads
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            // Set loading state and clear previous errors
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val response = apiService.getBahanMakanan()

                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()
                    // On success, update the list and stop loading
                    _uiState.update {
                        it.copy(isLoading = false, foodList = data)
                    }
                } else {
                    // On failure, set an error message for the UI to display
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Failed to load food data: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                // On exception (e.g., no internet), set an error message
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "An unknown error occurred.")
                }
            }
        }
    }

    /**
     * Clears the error message from the UI state.
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

