package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object Makanans : IntIdTable("makanan") {
    val name         = varchar("nama_makanan", 255)
    val description  = text("deskripsi_makanan")
    val videoTutorial = varchar("video_tutorial_makanan", 255).nullable()
}

@Serializable
data class MakananDTO(
    val id: Int? = null,
    val name: String,
    val description: String,
    val videoTutorial: String?
)

fun ResultRow.toMakananDTO() = MakananDTO(
    id            = this[Makanans.id].value,
    name          = this[Makanans.name],
    description   = this[Makanans.description],
    videoTutorial = this[Makanans.videoTutorial]
)