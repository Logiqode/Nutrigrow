package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import java.math.BigDecimal
import java.time.LocalDateTime

object Stuntings : IntIdTable("stunting") {
    val weight = decimal("berat_badan", 6, 2)  // e.g. 12.34 kg
    val height = decimal("tinggi_badan", 6, 2) // e.g.  85.50 cm
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
    val weight: BigDecimal,
    val height: BigDecimal,
    val ageInMonths: Int = 0,
    val notes: String? = null,
    val prediction: String,
    val confidenceScore: BigDecimal? = null,
    val userId: Int,
    val measuredAt: String,
    val createdAt: String? = null
)

fun ResultRow.toStuntingDTO() = StuntingDTO(
    id = this[Stuntings.id].value,
    weight = this[Stuntings.weight],
    height = this[Stuntings.height],
    ageInMonths = this[Stuntings.ageInMonths],
    notes = this[Stuntings.notes],
    prediction = this[Stuntings.prediction],
    confidenceScore = this[Stuntings.confidenceScore],
    userId = this[Stuntings.user].value,
    measuredAt = this[Stuntings.measuredAt].toString(),
    createdAt = this[Stuntings.createdAt].toString()
)