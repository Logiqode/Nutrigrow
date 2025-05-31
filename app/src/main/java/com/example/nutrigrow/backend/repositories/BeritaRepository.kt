package com.example.nutrigrow.backend.repositories

import com.example.nutrigrow.backend.models.BeritaDTO
import com.example.nutrigrow.backend.models.Beritas
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import com.example.nutrigrow.backend.models.toBeritaDTO
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class BeritaRepository {
    suspend fun getAll(): List<BeritaDTO> = newSuspendedTransaction {
        Beritas.selectAll().map { it.toBeritaDTO() }
    }

    suspend fun getById(id: Int): BeritaDTO? = newSuspendedTransaction {
        Beritas.select { Beritas.id eq id }.singleOrNull()?.toBeritaDTO()
    }

    suspend fun create(berita: BeritaDTO): BeritaDTO = newSuspendedTransaction {
        val id = Beritas.insert {
            it[title] = berita.title
            it[subtitle] = berita.subtitle
            it[content] = berita.content
            it[category] = berita.categoryId?.let { catId -> catId }
            it[createdAt] = LocalDateTime.parse(berita.createdAt)
        } get Beritas.id

        berita.copy(id = id.value)
    }

    suspend fun update(id: Int, berita: BeritaDTO): Boolean = newSuspendedTransaction {
        Beritas.update({ Beritas.id eq id }) {
            it[title] = berita.title
            it[subtitle] = berita.subtitle
            it[content] = berita.content
            it[category] = berita.categoryId
        } > 0
    }

    suspend fun delete(id: Int): Boolean = newSuspendedTransaction {
        Beritas.deleteWhere { Beritas.id eq id } > 0
    }

    suspend fun getByCategory(categoryId: Int): List<BeritaDTO> = newSuspendedTransaction {
        Beritas.select { Beritas.category eq categoryId }.map { it.toBeritaDTO() }
    }
}