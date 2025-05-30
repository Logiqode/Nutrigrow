package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ResultRow
import java.math.BigDecimal

object MakananBahan : Table("makanan_bahan_makanan") {
    val makanan = reference(
        "makanan_id_makanan",
        Makanans,
        onDelete = ReferenceOption.CASCADE
    )
    val bahan = reference(
        "bahan_makanan_id_bahan_makanan",
        BahanMakanans,
        onDelete = ReferenceOption.CASCADE
    )
    val quantity = decimal("quantity", 10, 2).default(BigDecimal.ONE)
    val unit = varchar("unit", 50).default("pcs")

    override val primaryKey = PrimaryKey(makanan, bahan, name = "PK_Makanan_Bahan")
}

@Serializable
data class MakananBahanDTO(
    val makananId: Int,
    val bahanId: Int,
    @Contextual val quantity: BigDecimal = BigDecimal.ONE,
    val unit: String = "pcs"
)

fun ResultRow.toMakananBahanDTO() = MakananBahanDTO(
    makananId = this[MakananBahan.makanan].value,
    bahanId = this[MakananBahan.bahan].value,
    quantity = this[MakananBahan.quantity],
    unit = this[MakananBahan.unit]
)