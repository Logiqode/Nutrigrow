package com.example.nutrigrow.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutrigrow.api.ApiService
import com.example.nutrigrow.api.RetrofitClient
import com.example.nutrigrow.di.SessionManager
import com.example.nutrigrow.ui.screens.auth.AuthViewModel
import com.example.nutrigrow.ui.screens.bahanmakanan.BahanMakananViewModel
import com.example.nutrigrow.ui.screens.calendar.CalendarViewModel
import com.example.nutrigrow.ui.screens.stunting.StuntingViewModel
import com.example.nutrigrow.ui.screens.user.UserViewModel

// The factory responsible for creating all ViewModels that have dependencies.
class ViewModelFactory(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                // Now it correctly provides both dependencies to AuthViewModel
                AuthViewModel(apiService, sessionManager) as T
            }
            modelClass.isAssignableFrom(BahanMakananViewModel::class.java) -> {
                BahanMakananViewModel(apiService) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(apiService) as T
            }
            modelClass.isAssignableFrom(StuntingViewModel::class.java) -> {
                StuntingViewModel(apiService) as T
            }
            modelClass.isAssignableFrom(CalendarViewModel::class.java) -> {
                CalendarViewModel(apiService) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    apiService = RetrofitClient.apiService,
                    sessionManager = SessionManager(context.applicationContext)
                ).also { instance = it }
            }
    }
}