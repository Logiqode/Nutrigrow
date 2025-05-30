package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.datetime

object KategoriBeritas : IntIdTable("kategori_berita") {
    val name = varchar("nama_kategori_berita", 255).uniqueIndex()
    val slug = varchar("slug", 255).uniqueIndex()
    val createdAt = datetime("created_at").defaultExpression(CurrentTimestamp())
}

@Serializable
data class KategoriBeritaDTO(
    val id: Int? = null,
    val name: String,
    val slug: String,
    val createdAt: String? = null
)

fun ResultRow.toKategoriBeritaDTO() = KategoriBeritaDTO(
    id = this[KategoriBeritas.id].value,
    name = this[KategoriBeritas.name],
    slug = this[KategoriBeritas.slug],
    createdAt = this[KategoriBeritas.createdAt].toString()
)