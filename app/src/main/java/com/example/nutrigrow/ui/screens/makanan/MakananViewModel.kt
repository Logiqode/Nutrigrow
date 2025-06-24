package com.example.nutrigrow.ui.screens.makanan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrigrow.api.ApiService
import com.example.nutrigrow.models.Makanan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MakananUiState(
    val makananList: List<Makanan> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MakananViewModel(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(MakananUiState())
    val uiState: StateFlow<MakananUiState> = _uiState.asStateFlow()

    init {
        loadMakanan()
    }

    fun loadMakanan() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val response = apiService.getMakanan()

                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()
                    _uiState.update {
                        it.copy(isLoading = false, makananList = data)
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Failed to load data: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "An unknown error occurred.")
                }
            }
        }
    }
}