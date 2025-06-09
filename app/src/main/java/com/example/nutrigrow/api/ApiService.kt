package com.example.nutrigrow.api

import com.example.nutrigrow.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/bahan-makanan")
    suspend fun getBahanMakanan(): Response<BahanMakananResponse>

    @POST("api/user")
    suspend fun register(@Body request: RegisterRequest): Response<UserResponse>

    @POST("api/user/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/user/me")
    suspend fun getMe(@Header("Authorization") token: String): Response<UserResponse>

    @PATCH("api/user")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body request: UpdateUserRequest
    ): Response<UserResponse>

}