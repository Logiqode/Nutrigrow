package com.example.nutrigrow.models

import com.google.gson.annotations.SerializedName

data class BahanMakananResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<BahanMakanan>,
    @SerializedName("meta") val meta: Meta
)

data class BahanMakanan(
    @SerializedName("id") val id: String,
    @SerializedName("nama_bahan_makanan") val nama: String,
    @SerializedName("deskripsi_bahan") val deskripsi: String
)

data class Meta(
    @SerializedName("page") val page: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("max_page") val maxPage: Int,
    @SerializedName("count") val count: Int
)