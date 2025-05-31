package com.example.nutrigrow.backend.services

import com.example.nutrigrow.backend.models.BeritaDTO
import com.example.nutrigrow.backend.repositories.BeritaRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BeritaService : KoinComponent {
    private val repository by inject<BeritaRepository>()

    suspend fun getAllBerita(): List<BeritaDTO> = repository.getAll()
    suspend fun getBeritaById(id: Int): BeritaDTO? = repository.getById(id)
    suspend fun createBerita(berita: BeritaDTO): BeritaDTO = repository.create(berita)
    suspend fun updateBerita(id: Int, berita: BeritaDTO): Boolean = repository.update(id, berita)
    suspend fun deleteBerita(id: Int): Boolean = repository.delete(id)
    suspend fun getBeritaByCategory(categoryId: Int): List<BeritaDTO> = repository.getByCategory(categoryId)
}