package com.example.nutrigrow.backend.utils

import com.example.nutrigrow.backend.models.* //
import com.example.nutrigrow.backend.repositories.* //
import com.example.nutrigrow.backend.services.UserService
import kotlinx.coroutines.Dispatchers // Added import
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction // Added import
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DatabaseSeeder : KoinComponent {

    private val userService by inject<UserService>()
    private val kategoriBeritaRepository by inject<KategoriBeritaRepository>()
    private val beritaRepository by inject<BeritaRepository>()
    private val bahanMakananRepository by inject<BahanMakananRepository>()
    private val makananRepository by inject<MakananRepository>()
    private val stuntingRepository by inject<StuntingRepository>()

    fun seed() {
        runBlocking {
            // Use newSuspendedTransaction for the entire block of seeding operations
            newSuspendedTransaction(Dispatchers.IO) { // Changed from transaction to newSuspendedTransaction
                addLogger(StdOutSqlLogger)

                if (kategoriBeritaRepository.getAll().isNotEmpty()) {
                    println("Database already seems to be seeded. Skipping.")
                    return@newSuspendedTransaction // Changed to return from newSuspendedTransaction
                }
                println("Seeding database...")

                // 1. Seed KategoriBerita
                val kategori1 = kategoriBeritaRepository.create(KategoriBeritaDTO(name = "Nutrisi Anak", slug = "nutrisi-anak"))
                val kategori2 = kategoriBeritaRepository.create(KategoriBeritaDTO(name = "Tips Parenting", slug = "tips-parenting"))
                println("Seeded KategoriBerita.")

                // 2. Seed Berita
                beritaRepository.create(
                    BeritaDTO(
                        title = "Pentingnya Sarapan Sehat untuk Anak",
                        subtitle = "Sarapan memberikan energi untuk memulai hari.",
                        content = "Detail lengkap mengenai pentingnya sarapan sehat...",
                        categoryId = kategori1.id, // kategori1 already has id and createdAt from repository
                        createdAt = kategori1.createdAt ?: LocalDateTime.now().toString(), // Use createdAt from Kategori or fallback
                        isPublished = true
                    )
                )
                beritaRepository.create(
                    BeritaDTO(
                        title = "Mengatasi Anak Susah Makan",
                        subtitle = "Strategi jitu menghadapi GTM pada anak.",
                        content = "Berbagai tips dan trik untuk mengatasi anak susah makan...",
                        categoryId = kategori2.id, // kategori2 already has id and createdAt from repository
                        createdAt = kategori2.createdAt ?: LocalDateTime.now().toString(), // Use createdAt from Kategori or fallback
                        isPublished = true
                    )
                )
                println("Seeded Berita.")

                // 3. Seed BahanMakanan
                val beras = bahanMakananRepository.create(BahanMakananDTO(name = "Beras Putih", description = "Sumber karbohidrat utama."))
                val telurAyam = bahanMakananRepository.create(BahanMakananDTO(name = "Telur Ayam", description = "Sumber protein hewani."))
                val wortel = bahanMakananRepository.create(BahanMakananDTO(name = "Wortel", description = "Sumber vitamin A."))
                val dagingAyam = bahanMakananRepository.create(BahanMakananDTO(name = "Daging Ayam", description = "Sumber protein hewani."))
                println("Seeded BahanMakanan.")

                // 4. Seed Makanan
                val nasiTimAyam = makananRepository.create(
                    MakananDTO(
                        name = "Nasi Tim Ayam Wortel",
                        description = "Nasi tim lembut dengan ayam dan wortel, cocok untuk bayi.",
                        videoTutorial = "https://youtube.com/example/nasitim", // Corrected URL
                        preparationTime = 30,
                        difficulty = Difficulty.EASY
                    )
                )
                // Ensure IDs are not null before using them
                if (nasiTimAyam.id != null && beras.id != null && dagingAyam.id != null && wortel.id != null) {
                    makananRepository.addBahan(MakananBahanDTO(makananId = nasiTimAyam.id!!, bahanId = beras.id!!, quantity = BigDecimal("100"), unit = "gram"))
                    makananRepository.addBahan(MakananBahanDTO(makananId = nasiTimAyam.id!!, bahanId = dagingAyam.id!!, quantity = BigDecimal("50"), unit = "gram"))
                    makananRepository.addBahan(MakananBahanDTO(makananId = nasiTimAyam.id!!, bahanId = wortel.id!!, quantity = BigDecimal("30"), unit = "gram"))
                }
                println("Seeded Makanan.")

                // 5. Seed Users & UserCredentials (using UserService)
                val user1DTO = UserDTO(
                    name = "John Doe",
                    address = "123 Main St, Anytown",
                    phone = "081234567890",
                    gender = 'L',
                    birthDate = LocalDate.of(1990, 1, 15).format(DateTimeFormatter.ISO_LOCAL_DATE)
                )
                // Create the user first to get their ID
                val createdUserEntity = userService.createUser(user1DTO) // Assuming createUser returns the UserDTO with ID

                if (createdUserEntity.id != null) {
                    val credential1DTO = UserCredentialDTO(
                        email = "john.doe@example.com",
                        password = "Password123!", // UserService will hash this
                        username = "johndoe",
                        userId = createdUserEntity.id!! // Use the ID from the created user
                    )

                    val registeredCredential = userService.registerUser(credential1DTO.copy(userId = 0), user1DTO)

                    println("Seeded User: johndoe.")

                    if (registeredCredential.userId != 0) {
                        // 6. Seed Stunting Data
                        stuntingRepository.create(StuntingDTO(
                            weight = BigDecimal("10.5"),
                            height = BigDecimal("75.0"),
                            ageInMonths = 12,
                            notes = "Pemeriksaan rutin bulanan.",
                            prediction = "Normal",
                            confidenceScore = BigDecimal("0.95"),
                            userId = registeredCredential.userId,
                            measuredAt = LocalDateTime.now().minusMonths(1).toString()
                        ))
                        stuntingRepository.create(StuntingDTO(
                            weight = BigDecimal("11.2"),
                            height = BigDecimal("78.5"),
                            ageInMonths = 15,
                            notes = "Pemeriksaan setelah imunisasi.",
                            prediction = "Normal",
                            confidenceScore = BigDecimal("0.93"),
                            userId = registeredCredential.userId,
                            measuredAt = LocalDateTime.now().toString()
                        ))
                        println("Seeded Stunting Data for johndoe.")
                    } else {
                        println("Failed to get a valid userId after user registration. Skipping stunting data seeding for johndoe.")
                    }
                } else {
                    println("Failed to create user 'John Doe'. Skipping dependent seeding.")
                }
                println("Database seeding complete.")
            }
        }
    }
}