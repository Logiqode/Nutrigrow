package com.example.nutrigrow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrigrow.api.ApiResponse
import com.example.nutrigrow.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _apiResponse = MutableStateFlow<ApiResponse?>(null)
    val apiResponse: StateFlow<ApiResponse?> = _apiResponse

    fun fetchData(route: String) {
//        viewModelScope.launch {
//            try {
//                val response = RetrofitClient.apiService.fetchData(route)
//                _apiResponse.value = response.body()
//            } catch (e: Exception) {
//                _apiResponse.value = null
//            }
//        }
    }
}