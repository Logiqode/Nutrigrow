package com.example.nutrigrow.api

import com.example.nutrigrow.models.BahanMakananResponse
import com.example.nutrigrow.models.LoginRequest
import com.example.nutrigrow.models.LoginResponse
import com.example.nutrigrow.models.RegisterRequest
import com.example.nutrigrow.models.UpdateProfileRequest
import com.example.nutrigrow.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @GET("api/bahan-makanan")
    suspend fun getBahanMakanan(): Response<BahanMakananResponse>

//    @GET("api/user")
//    suspend fun getAllUsers(@Header("Authorization") token: String): Response<GetAllUserResponse>

    @POST("api/user")
    suspend fun register(@Body request: RegisterRequest): Response<UserResponse>

    @POST("api/user/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/user/me")
    suspend fun getMe(@Header("Authorization") token: String): Response<UserResponse>

    @PUT("api/user/me")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<UserResponse>

}