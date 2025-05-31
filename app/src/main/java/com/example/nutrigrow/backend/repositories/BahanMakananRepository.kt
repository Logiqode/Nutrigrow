package com.example.nutrigrow.backend.repositories

import com.example.nutrigrow.backend.models.BahanMakananDTO
import com.example.nutrigrow.backend.models.BahanMakanans
import com.example.nutrigrow.backend.models.toBahanMakananDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class BahanMakananRepository {
    suspend fun getAll(): List<BahanMakananDTO> = newSuspendedTransaction {
        BahanMakanans.selectAll().map { it.toBahanMakananDTO() }
    }

    suspend fun getById(id: Int): BahanMakananDTO? = newSuspendedTransaction {
        BahanMakanans.select { BahanMakanans.id eq id }.singleOrNull()?.toBahanMakananDTO()
    }

    suspend fun create(bahan: BahanMakananDTO): BahanMakananDTO = newSuspendedTransaction {
        val id = BahanMakanans.insert {
            it[name] = bahan.name
            it[description] = bahan.description
        } get BahanMakanans.id

        bahan.copy(id = id.value)
    }

    suspend fun update(id: Int, bahan: BahanMakananDTO): Boolean = newSuspendedTransaction {
        BahanMakanans.update({ BahanMakanans.id eq id }) {
            it[name] = bahan.name
            it[description] = bahan.description
        } > 0
    }

    suspend fun delete(id: Int): Boolean = newSuspendedTransaction {
        BahanMakanans.deleteWhere { BahanMakanans.id eq id } > 0
    }
}