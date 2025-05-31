package com.example.nutrigrow.backend.repositories

import com.example.nutrigrow.backend.models.StuntingDTO
import com.example.nutrigrow.backend.models.Stuntings
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.math.BigDecimal
import com.example.nutrigrow.backend.models.toStuntingDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class StuntingRepository {
    suspend fun getAll(): List<StuntingDTO> = newSuspendedTransaction {
        Stuntings.selectAll().map { it.toStuntingDTO() }
    }

    suspend fun getById(id: Int): StuntingDTO? = newSuspendedTransaction {
        Stuntings.select { Stuntings.id eq id }.singleOrNull()?.toStuntingDTO()
    }

    suspend fun create(record: StuntingDTO): StuntingDTO = newSuspendedTransaction {
        val id = Stuntings.insert {
            it[weight] = record.weight
            it[height] = record.height
            it[notes] = record.notes
            it[prediction] = record.prediction
            it[user] = record.userId
        } get Stuntings.id

        record.copy(id = id.value)
    }

    suspend fun update(id: Int, record: StuntingDTO): Boolean = newSuspendedTransaction {
        Stuntings.update({ Stuntings.id eq id }) {
            it[weight] = record.weight
            it[height] = record.height
            it[notes] = record.notes
            it[prediction] = record.prediction
        } > 0
    }

    suspend fun delete(id: Int): Boolean = newSuspendedTransaction {
        Stuntings.deleteWhere { Stuntings.id eq id } > 0
    }

    suspend fun getByUser(userId: Int): List<StuntingDTO> = newSuspendedTransaction {
        Stuntings.select { Stuntings.user eq userId }.map { it.toStuntingDTO() }
    }

    suspend fun predictRisk(weight: BigDecimal, height: BigDecimal, ageInMonths: Int): String {

        // TODO: Call ML API for the Results
        return "Normal" // Placeholder
    }
}