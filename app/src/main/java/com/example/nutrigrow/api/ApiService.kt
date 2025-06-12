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

    // The @Header parameter is no longer needed! The interceptor handles it.
    @GET("api/user/me")
    suspend fun getMe(): Response<UserResponse>

    // This one also no longer needs the token parameter.
    @PATCH("api/user")
    suspend fun updateUser(@Body request: UpdateUserRequest): Response<UserResponse>
}