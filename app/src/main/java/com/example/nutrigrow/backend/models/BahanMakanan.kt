package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object BahanMakanans : IntIdTable("bahan_makanan") {
    val name        = varchar("nama_bahan_makanan", 255)
    val description = text("deskripsi_bahan")
}

@Serializable
data class BahanMakananDTO(
    val id: Int? = null,
    val name: String,
    val description: String
)

fun ResultRow.toBahanMakananDTO() = BahanMakananDTO(
    id          = this[BahanMakanans.id].value,
    name        = this[BahanMakanans.name],
    description = this[BahanMakanans.description]
)