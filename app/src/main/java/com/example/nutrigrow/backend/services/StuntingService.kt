package com.example.nutrigrow.backend.services

import com.example.nutrigrow.backend.models.StuntingDTO
import com.example.nutrigrow.backend.repositories.StuntingRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.math.BigDecimal

class StuntingService : KoinComponent {
    private val repository by inject<StuntingRepository>()

    suspend fun getAllStuntingRecords(): List<StuntingDTO> = repository.getAll()
    suspend fun getStuntingRecordById(id: Int): StuntingDTO? = repository.getById(id)
    suspend fun createStuntingRecord(record: StuntingDTO): StuntingDTO = repository.create(record)
    suspend fun updateStuntingRecord(id: Int, record: StuntingDTO): Boolean = repository.update(id, record)
    suspend fun deleteStuntingRecord(id: Int): Boolean = repository.delete(id)
    suspend fun getStuntingRecordsByUser(userId: Int): List<StuntingDTO> = repository.getByUser(userId)
    suspend fun predictStuntingRisk(jenisKelamin: Int, height: BigDecimal, ageInMonths: Int): String {
        return repository.predictRisk(jenisKelamin, height, ageInMonths)
    }
}