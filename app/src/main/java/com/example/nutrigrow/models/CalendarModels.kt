package com.example.nutrigrow.models

import com.google.gson.annotations.SerializedName
import java.util.UUID

// Calendar request for getting stunting data by month/year
data class StuntingCalendarRequest(
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int
)

// Calendar note creation request (creates a stunting record)
data class CalendarNoteCreateRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("jenis_kelamin") val jenisKelamin: String, // "Laki-laki" or "Perempuan"
    @SerializedName("tinggi_badan") val tinggiBadan: Double,
    @SerializedName("catatan_stunting") val catatanStunting: String
)

// Calendar note update request
data class CalendarNoteUpdateRequest(
    @SerializedName("jenis_kelamin") val jenisKelamin: String?,
    @SerializedName("tinggi_badan") val tinggiBadan: Double?,
    @SerializedName("catatan_stunting") val catatanStunting: String?
)

// Child data model for UI
data class Child(
    val id: String,
    val name: String,
    val jenisKelamin: String, // "Laki-laki" or "Perempuan"
    val birthDate: String? = null
)

// Calendar note for UI (maps to StuntingResponse)
data class CalendarNote(
    val id: UUID,
    val childId: String, // This will be the user_id in stunting record
    val childName: String,
    val jenisKelamin: String,
    val date: String, // formatted date from created_at
    val height: Double,
    val notes: String,
    val hasilPrediksi: String? = null
)

