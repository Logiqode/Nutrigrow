package com.example.nutrigrow.api

import com.example.nutrigrow.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/bahan-makanan")
    suspend fun getBahanMakanan(): Response<ApiResponse<List<BahanMakanan>>>

    @POST("api/user")
    // Assumes registration returns the new UserResponse inside the 'data' object
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<UserResponse>>

    @POST("api/user/login")
    // Using ApiResponse<LoginData> is more consistent than our old LoginResponse
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginData>>

    @GET("api/user/me")
    suspend fun getMe(): Response<ApiResponse<UserResponse>>

    @PATCH("api/user")
    // Assumes updating the user returns the updated UserResponse inside the 'data' object
    suspend fun updateUser(@Body request: UpdateUserRequest): Response<ApiResponse<UserResponse>>

    @POST("api/stunting/predict")
    suspend fun predictStunting(@Body request: StuntingPredictRequest): Response<ApiResponse<StuntingPredictResponse>>

    // Calendar endpoints
    @GET("api/stunting/calendar")
    suspend fun getStuntingCalendar(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<ApiResponse<List<StuntingResponse>>>

    @POST("api/stunting")
    suspend fun createStuntingRecord(@Body request: CalendarNoteCreateRequest): Response<ApiResponse<StuntingResponse>>

    @PUT("api/stunting/{id}")
    suspend fun updateStuntingRecord(
        @Path("id") id: String,
        @Body request: CalendarNoteUpdateRequest
    ): Response<ApiResponse<StuntingResponse>>

    @DELETE("api/stunting/{id}")
    suspend fun deleteStuntingRecord(@Path("id") id: String): Response<ApiResponse<Unit>>

    @GET("api/makanan")
    suspend fun getMakanan(): Response<ApiResponse<List<Makanan>>>


}