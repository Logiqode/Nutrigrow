package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ResultRow

object MakananBahan : Table("makanan_bahan_makanan") {
    val makanan = reference(
        "makanan_id_makanan",
        Makanans,
        onDelete = ReferenceOption.CASCADE
    )
    val bahan   = reference(
        "bahan_makanan_id_bahan_makanan",
        BahanMakanans,
        onDelete = ReferenceOption.CASCADE
    )
    override val primaryKey = PrimaryKey(makanan, bahan, name = "PK_Makanan_Bahan")
}

@Serializable
data class MakananBahanDTO(
    val makananId: Int,
    val bahanId: Int
)

fun ResultRow.toMakananBahanDTO() = MakananBahanDTO(
    makananId = this[MakananBahan.makanan].value,
    bahanId   = this[MakananBahan.bahan].value
)