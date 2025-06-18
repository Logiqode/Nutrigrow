package com.example.nutrigrow.models

import com.google.gson.annotations.SerializedName

// This represents the main food item object
data class Makanan(
    @SerializedName("id") val id: String,
    @SerializedName("nama_makanan") val nama: String,
    @SerializedName("deskripsi_makanan") val deskripsi: String,
    @SerializedName("video_tutorial_makanan") val videoUrl: String,
    @SerializedName("bahan_makanans") val ingredients: List<BahanMakananSimple>
)

// This represents the simplified ingredient object nested within a Makanan object
data class BahanMakananSimple(
    @SerializedName("id") val id: String,
    @SerializedName("nama_bahan_makanan") val nama: String
)