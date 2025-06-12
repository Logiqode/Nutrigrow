package com.example.nutrigrow.models

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Stunting(
    @SerializedName("id") val id: UUID,
    @SerializedName("user_id") val userId: UUID,
    @SerializedName("jenis_kelamin") val jenisKelamin: Int,
    @SerializedName("tinggi_badan") val tinggiBadan: Double,
    @SerializedName("catatan_stunting") val catatanStunting: String,
    @SerializedName("hasil_prediksi") val hasilPrediksi: String,
    @SerializedName("user") val user: UserResponse? = null
)

data class StuntingCreateRequest(
    @SerializedName("user_id") val userId: UUID,
    @SerializedName("jenis_kelamin") val jenisKelamin: String,
    @SerializedName("umur_bulan") val umurBulan: Int,
    @SerializedName("tinggi_badan") val tinggiBadan: Double,
    @SerializedName("catatan_stunting") val catatanStunting: String
)

data class StuntingUpdateRequest(
    @SerializedName("jenis_kelamin") val jenisKelamin: String?,
    @SerializedName("tinggi_badan") val tinggiBadan: Double?,
    @SerializedName("catatan_stunting") val catatanStunting: String?,
    @SerializedName("hasil_prediksi") val hasilPrediksi: String?
)

data class StuntingResponse(
    @SerializedName("id") val id: UUID,
    @SerializedName("user_id") val userId: UUID,
    @SerializedName("jenis_kelamin") val jenisKelamin: String,
    @SerializedName("tinggi_badan") val tinggiBadan: Double,
    @SerializedName("catatan_stunting") val catatanStunting: String,
    @SerializedName("hasil_prediksi") val hasilPrediksi: String,
    @SerializedName("user") val user: UserResponse? = null
)

data class StuntingPredictRequest(
    // FIX: Corrected the JSON key to snake_case
    @SerializedName("umur_bulan")
    val umurBulan: Int,

    // FIX: Corrected the JSON key to snake_case
    @SerializedName("jenis_kelamin")
    val jenisKelamin: String,

    // FIX: Corrected the JSON key to snake_case
    @SerializedName("tinggi_badan")
    val tinggiBadan: Int
)

data class StuntingPredictResponse(
    @SerializedName("status_gizi") val statusGizi: String,
    @SerializedName("confidence") val confidence: Double
)

data class StuntingPaginationResponse(
    @SerializedName("data") val data: List<StuntingResponse>,
    @SerializedName("pagination") val pagination: PaginationResponse
)

data class PaginationResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("max_page") val maxPage: Int,
    @SerializedName("count") val count: Int
)