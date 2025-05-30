package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.datetime

object BahanMakanans : IntIdTable("bahan_makanan") {
    val name = varchar("nama_bahan_makanan", 255).uniqueIndex()
    val description = text("deskripsi_bahan")
    val createdAt = datetime("created_at").defaultExpression(CurrentTimestamp())
    val updatedAt = datetime("updated_at").nullable()
}

@Serializable
data class BahanMakananDTO(
    val id: Int? = null,
    val name: String,
    val description: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

fun ResultRow.toBahanMakananDTO() = BahanMakananDTO(
    id = this[BahanMakanans.id].value,
    name = this[BahanMakanans.name],
    description = this[BahanMakanans.description],
    createdAt = this[BahanMakanans.createdAt].toString(),
    updatedAt = this[BahanMakanans.updatedAt]?.toString()
)