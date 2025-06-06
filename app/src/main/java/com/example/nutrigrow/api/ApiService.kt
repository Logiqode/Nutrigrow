package com.example.nutrigrow.api

import com.example.nutrigrow.models.BahanMakananResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("api/bahan-makanan")
    suspend fun getBahanMakanan(): Response<BahanMakananResponse>
}