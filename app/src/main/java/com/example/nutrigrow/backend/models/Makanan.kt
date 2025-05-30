package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Makanans : IntIdTable("makanan") {
    val name = varchar("nama_makanan", 255).uniqueIndex()
    val description = text("deskripsi_makanan")
    val videoTutorial = varchar("video_tutorial_makanan", 255).nullable()
    val preparationTime = integer("preparation_time").default(0) // in minutes
    val difficulty = enumeration("difficulty_level", Difficulty::class).default(Difficulty.MEDIUM)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").nullable()
}

enum class Difficulty { EASY, MEDIUM, HARD }

@Serializable
data class MakananDTO(
    val id: Int? = null,
    val name: String,
    val description: String,
    val videoTutorial: String? = null,
    val preparationTime: Int = 0,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

fun ResultRow.toMakananDTO() = MakananDTO(
    id = this[Makanans.id].value,
    name = this[Makanans.name],
    description = this[Makanans.description],
    videoTutorial = this[Makanans.videoTutorial],
    preparationTime = this[Makanans.preparationTime],
    difficulty = this[Makanans.difficulty],
    createdAt = this[Makanans.createdAt].toString(),
    updatedAt = this[Makanans.updatedAt]?.toString()
)