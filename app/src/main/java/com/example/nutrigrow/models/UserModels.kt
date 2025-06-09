package com.example.nutrigrow.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
){

}

data class LoginRequest(
    val email: String,
    val password: String
){

}

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserResponse
){

}

data class UpdateProfileRequest(
    val name: String,
    val email: String,
    val gender: String? = null
)

data class UserResponse(
    val id: String,
    val email: String,
    val name: String,
    @SerializedName("telp_number") val telpNumber: String = "",
    val role: String = "",
    @SerializedName("image_url") val imageUrl: String = "",
    @SerializedName("is_verified") val isVerified: Boolean = false,
    val gender: String? = null
)
