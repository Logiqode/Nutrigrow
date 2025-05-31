package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Beritas : IntIdTable("berita") {
    val title = varchar("judul_berita", 255).index()
    val subtitle = varchar("subjudul_berita", 255)
    val content = text("deskripsi_berita")
    val category = reference(
        "kategori_berita_id",
        KategoriBeritas,
        onDelete = ReferenceOption.SET_NULL
    ).nullable()
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").nullable()
    val isPublished = bool("is_published").default(false)
}

@Serializable
data class BeritaDTO(
    val id: Int? = null,
    val title: String,
    val subtitle: String,
    val content: String,
    val categoryId: Int?,
    val createdAt: String,
    val updatedAt: String? = null,
    val isPublished: Boolean = false
)

fun ResultRow.toBeritaDTO() = BeritaDTO(
    id = this[Beritas.id].value,
    title = this[Beritas.title],
    subtitle = this[Beritas.subtitle],
    content = this[Beritas.content],
    categoryId = this[Beritas.category]?.value,
    createdAt = this[Beritas.createdAt].toString(),
    updatedAt = this[Beritas.updatedAt]?.toString(),
    isPublished = this[Beritas.isPublished]
)