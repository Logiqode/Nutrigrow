package com.example.nutrigrow.ui.screens.stunting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrigrow.api.ApiService
import com.example.nutrigrow.models.StuntingPredictRequest
import com.example.nutrigrow.models.StuntingPredictResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StuntingUiState(
    val predictionResult: StuntingPredictResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class StuntingViewModel(private val apiService: ApiService) : ViewModel() {

    private val _uiState = MutableStateFlow(StuntingUiState())
    val uiState: StateFlow<StuntingUiState> = _uiState.asStateFlow()

    // FIX: jenisKelamin is now a String again
    fun predictStunting(umurBulan: Int, jenisKelamin: String, tinggiBadan: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val request = StuntingPredictRequest(umurBulan, jenisKelamin, tinggiBadan)
                val response = apiService.predictStunting(request)
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            predictionResult = response.body()?.data
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Prediction Failed: ${response.code()} ${response.message()}. Details: ${errorBody ?: "No details"}"
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

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}