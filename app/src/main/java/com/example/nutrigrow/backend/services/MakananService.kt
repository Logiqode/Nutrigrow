package com.example.nutrigrow.backend.services

import com.example.nutrigrow.backend.models.MakananBahanDTO
import com.example.nutrigrow.backend.models.MakananDTO
import com.example.nutrigrow.backend.repositories.MakananRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MakananService : KoinComponent {
    private val repository by inject<MakananRepository>()

    suspend fun getAllMakanan(): List<MakananDTO> = repository.getAll()
    suspend fun getMakananById(id: Int): MakananDTO? = repository.getById(id)
    suspend fun createMakanan(makanan: MakananDTO): MakananDTO = repository.create(makanan)
    suspend fun updateMakanan(id: Int, makanan: MakananDTO): Boolean = repository.update(id, makanan)
    suspend fun deleteMakanan(id: Int): Boolean = repository.delete(id)

    suspend fun addBahanToMakanan(makananBahan: MakananBahanDTO): Boolean = repository.addBahan(makananBahan)
    suspend fun removeBahanFromMakanan(makananBahan: MakananBahanDTO): Boolean = repository.removeBahan(makananBahan)
    suspend fun getBahanForMakanan(makananId: Int): List<Int> = repository.getBahan(makananId)
}