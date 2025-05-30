package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import java.math.BigDecimal

object Stuntings : IntIdTable("stunting") {
    val weight     = decimal("berat_badan", 6, 2)  // e.g. 12.34 kg
    val height     = decimal("tinggi_badan", 6, 2) // e.g.  85.50 cm
    val notes      = varchar("catatan_stunting", 255)
    val prediction = varchar("hasil_prediksi", 50)
    val user       = reference("user_id_user", Users, onDelete = ReferenceOption.CASCADE)
}

@Serializable
data class StuntingDTO(
    val id: Int? = null,
    val weight: BigDecimal,
    val height: BigDecimal,
    val notes: String,
    val prediction: String,
    val userId: Int
)

fun ResultRow.toStuntingDTO() = StuntingDTO(
    id         = this[Stuntings.id].value,
    weight     = this[Stuntings.weight],
    height     = this[Stuntings.height],
    notes      = this[Stuntings.notes],
    prediction = this[Stuntings.prediction],
    userId     = this[Stuntings.user].value
)