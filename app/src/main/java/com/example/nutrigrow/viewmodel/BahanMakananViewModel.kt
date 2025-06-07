package com.example.nutrigrow.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrigrow.api.RetrofitClient
import com.example.nutrigrow.models.BahanMakanan
import kotlinx.coroutines.launch

class BahanMakananViewModel : ViewModel() {
    private val _bahanMakananList = mutableStateOf<List<BahanMakanan>>(emptyList())
    val bahanMakananList: State<List<BahanMakanan>> = _bahanMakananList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun loadBahanMakanan() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("API", "Attempting to fetch from: ${RetrofitClient.BASE_URL}api/bahan-makanan")
                val response = RetrofitClient.apiService.getBahanMakanan()
                Log.d("API", "Response code: ${response.code()}")
                Log.d("API", "Response body: ${response.body()}")

                if (response.isSuccessful) {
                    _bahanMakananList.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("API", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}