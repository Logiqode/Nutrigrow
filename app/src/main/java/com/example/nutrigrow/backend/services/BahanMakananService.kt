package com.example.nutrigrow.backend.services

import com.example.nutrigrow.backend.models.BahanMakananDTO
import com.example.nutrigrow.backend.repositories.BahanMakananRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BahanMakananService : KoinComponent {
    private val repository by inject<BahanMakananRepository>()

    suspend fun getAllBahanMakanan(): List<BahanMakananDTO> = repository.getAll()
    suspend fun getBahanMakananById(id: Int): BahanMakananDTO? = repository.getById(id)
    suspend fun createBahanMakanan(bahan: BahanMakananDTO): BahanMakananDTO = repository.create(bahan)
    suspend fun updateBahanMakanan(id: Int, bahan: BahanMakananDTO): Boolean = repository.update(id, bahan)
    suspend fun deleteBahanMakanan(id: Int): Boolean = repository.delete(id)
}