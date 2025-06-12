package com.example.nutrigrow.api

import com.google.gson.annotations.SerializedName

/**
 * A generic wrapper for all API responses from your server.
 * The 'T' allows this class to be used for any type of data,
 * e.g., ApiResponse<UserResponse>, ApiResponse<List<BahanMakanan>>.
 */
data class ApiResponse<T>(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: T? // The actual data payload (e.g., UserResponse) can be nullable
)