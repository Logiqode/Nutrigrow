package com.example.nutrigrow.backend.repositories

import com.example.nutrigrow.backend.models.MakananBahan
import com.example.nutrigrow.backend.models.MakananBahanDTO
import com.example.nutrigrow.backend.models.MakananDTO
import com.example.nutrigrow.backend.models.Makanans
import com.example.nutrigrow.backend.models.toMakananDTO
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class MakananRepository {
    suspend fun getAll(): List<MakananDTO> = newSuspendedTransaction {
        Makanans.selectAll().map { it.toMakananDTO() }
    }

    suspend fun getById(id: Int): MakananDTO? = newSuspendedTransaction {
        Makanans.select { Makanans.id eq id }.singleOrNull()?.toMakananDTO()
    }

    suspend fun create(makanan: MakananDTO): MakananDTO = newSuspendedTransaction {
        val id = Makanans.insert {
            it[name] = makanan.name
            it[description] = makanan.description
            it[videoTutorial] = makanan.videoTutorial
        } get Makanans.id

        makanan.copy(id = id.value)
    }

    suspend fun update(id: Int, makanan: MakananDTO): Boolean = newSuspendedTransaction {
        Makanans.update({ Makanans.id eq id }) {
            it[name] = makanan.name
            it[description] = makanan.description
            it[videoTutorial] = makanan.videoTutorial
        } > 0
    }

    suspend fun delete(id: Int): Boolean = newSuspendedTransaction {
        Makanans.deleteWhere { Makanans.id eq id } > 0
    }

    suspend fun addBahan(makananBahan: MakananBahanDTO): Boolean = newSuspendedTransaction {
        MakananBahan.insert {
            it[makanan] = makananBahan.makananId
            it[bahan] = makananBahan.bahanId
        }.insertedCount > 0
    }

    suspend fun removeBahan(makananBahan: MakananBahanDTO): Boolean = newSuspendedTransaction {
        MakananBahan.deleteWhere {
            (MakananBahan.makanan eq makananBahan.makananId) and
                    (MakananBahan.bahan eq makananBahan.bahanId)
        } > 0
    }

    suspend fun getBahan(makananId: Int): List<Int> = newSuspendedTransaction {
        MakananBahan.select { MakananBahan.makanan eq makananId }.map { it[MakananBahan.bahan].value }
    }
}