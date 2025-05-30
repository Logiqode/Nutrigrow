package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object KategoriBeritas : IntIdTable("kategori_berita") {
    val name = varchar("nama_kategori_berita", 255)
}

@Serializable
data class KategoriBeritaDTO(
    val id: Int? = null,
    val name: String
)

fun ResultRow.toKategoriBeritaDTO() = KategoriBeritaDTO(
    id   = this[KategoriBeritas.id].value,
    name = this[KategoriBeritas.name]
)