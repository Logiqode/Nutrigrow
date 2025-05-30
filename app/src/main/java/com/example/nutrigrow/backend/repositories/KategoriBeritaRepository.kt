package com.example.nutrigrow.backend.repositories

import com.example.nutrigrow.backend.models.KategoriBeritaDTO
import com.example.nutrigrow.backend.models.KategoriBeritas
import com.example.nutrigrow.backend.models.toKategoriBeritaDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class KategoriBeritaRepository {

    // Public suspend fun getById - keeps its own transaction for external calls
    suspend fun getById(id: Int): KategoriBeritaDTO? = newSuspendedTransaction {
        fetchKategoriByIdInternal(id) // Calls the internal, non-transaction-starting version
    }

    // Internal, non-suspending function that assumes it's already within an active transaction.
    // It does not start a new transaction.
    private fun fetchKategoriByIdInternal(id: Int): KategoriBeritaDTO? {
        return KategoriBeritas.select { KategoriBeritas.id eq id }
            .singleOrNull()
            ?.toKategoriBeritaDTO()
    }

    suspend fun create(kategori: KategoriBeritaDTO): KategoriBeritaDTO = newSuspendedTransaction {
        // This is Transaction A
        val newId = KategoriBeritas.insertAndGetId {
            it[name] = kategori.name
            it[slug] = kategori.slug
            // createdAt is automatically handled by defaultExpression in table definition
        }

        // Call the internal fetch function. It will operate within Transaction A.
        fetchKategoriByIdInternal(newId.value)
            ?: throw IllegalStateException("Failed to retrieve created KategoriBerita with id ${newId.value}")
    }

    suspend fun getAll(): List<KategoriBeritaDTO> = newSuspendedTransaction {
        KategoriBeritas.selectAll()
            .map { it.toKategoriBeritaDTO() }
    }

    suspend fun update(id: Int, kategori: KategoriBeritaDTO): Boolean = newSuspendedTransaction {
        KategoriBeritas.update({ KategoriBeritas.id eq id }) {
            it[name] = kategori.name
            it[slug] = kategori.slug
        } > 0
    }

    suspend fun delete(id: Int): Boolean = newSuspendedTransaction {
        KategoriBeritas.deleteWhere { KategoriBeritas.id eq id } > 0
    }

    suspend fun getBySlug(slug: String): KategoriBeritaDTO? = newSuspendedTransaction {
        KategoriBeritas.select { KategoriBeritas.slug eq slug }
            .singleOrNull()
            ?.toKategoriBeritaDTO()
    }
}