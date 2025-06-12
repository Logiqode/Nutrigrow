package com.example.nutrigrow.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

// 1. Create this new class to represent the nested "data" object in the JSON
data class LoginData(
    @SerializedName("access_token")
    val accessToken: String?, // Use @SerializedName to match the JSON field name

    @SerializedName("refresh_token")
    val refreshToken: String?
)

// 2. Update your LoginResponse to match the overall JSON structure
data class LoginResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: LoginData? // This property holds the nested LoginData object
)

data class UpdateUserRequest(
    val name: String,
    val email: String,
    val password: String
)

data class UserResponse(
    val id: String,
    val email: String,
    val name: String,
    @SerializedName("telp_number") val telpNumber: String = "",
    val role: String = "",
    @SerializedName("image_url") val imageUrl: String = "",
    @SerializedName("is_verified") val isVerified: Boolean = false,
)
