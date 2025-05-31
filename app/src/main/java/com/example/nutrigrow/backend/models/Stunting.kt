package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import java.math.BigDecimal
import java.time.LocalDateTime

object Stunting : IntIdTable("stunting") {
    val weight = decimal("berat_badan", 6, 2)
    val height = decimal("tinggi_badan", 6, 2)
    val ageInMonths = integer("age_in_months").default(0)
    val notes = varchar("catatan_stunting", 255).nullable()
    val prediction = varchar("hasil_prediksi", 50)
    val confidenceScore = decimal("confidence_score", 3, 2).nullable()
    val user = reference("user_id_user", Users, onDelete = ReferenceOption.CASCADE)
    val measuredAt = datetime("measured_at").clientDefault { LocalDateTime.now() }
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
}

@Serializable
data class StuntingDTO(
    val id: Int? = null,
    @Contextual val weight: BigDecimal,
    @Contextual val height: BigDecimal,
    val ageInMonths: Int = 0,
    val notes: String? = null,
    val prediction: String,
    @Contextual val confidenceScore: BigDecimal? = null,
    val userId: Int,
    val measuredAt: String,
    val createdAt: String? = null
)

fun ResultRow.toStuntingDTO() = StuntingDTO(
    id = this[Stunting.id].value,
    weight = this[Stunting.weight],
    height = this[Stunting.height],
    ageInMonths = this[Stunting.ageInMonths],
    notes = this[Stunting.notes],
    prediction = this[Stunting.prediction],
    confidenceScore = this[Stunting.confidenceScore],
    userId = this[Stunting.user].value,
    measuredAt = this[Stunting.measuredAt].toString(),
    createdAt = this[Stunting.createdAt].toString()
)