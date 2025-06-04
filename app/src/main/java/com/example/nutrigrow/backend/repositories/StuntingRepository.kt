package com.example.nutrigrow.backend.repositories

import com.example.nutrigrow.backend.models.StuntingDTO
import com.example.nutrigrow.backend.models.Stunting
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.math.BigDecimal
import java.net.HttpURLConnection
import java.net.URL
import com.example.nutrigrow.backend.models.toStuntingDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class StuntingRepository {
    protected val apiURL = "http://127.0.0.1:8080/predict"
    private val jsonFormat = Json { ignoreUnknownKeys = true }

    suspend fun getAll(): List<StuntingDTO> = newSuspendedTransaction {
        Stunting.selectAll().map { it.toStuntingDTO() }
    }

    suspend fun getById(id: Int): StuntingDTO? = newSuspendedTransaction {
        Stunting.select { Stunting.id eq id }.singleOrNull()?.toStuntingDTO()
    }

    suspend fun create(record: StuntingDTO): StuntingDTO = newSuspendedTransaction {
        val id = Stunting.insert {
            it[weight] = record.weight
            it[height] = record.height
            it[notes] = record.notes
            it[prediction] = record.prediction
            it[user] = record.userId
        } get Stunting.id

        record.copy(id = id.value)
    }

    suspend fun update(id: Int, record: StuntingDTO): Boolean = newSuspendedTransaction {
        Stunting.update({ Stunting.id eq id }) {
            it[weight] = record.weight
            it[height] = record.height
            it[notes] = record.notes
            it[prediction] = record.prediction
        } > 0
    }

    suspend fun delete(id: Int): Boolean = newSuspendedTransaction {
        Stunting.deleteWhere { Stunting.id eq id } > 0
    }

    suspend fun getByUser(userId: Int): List<StuntingDTO> = newSuspendedTransaction {
        Stunting.select { Stunting.user eq userId }.map { it.toStuntingDTO() }
    }

    @Serializable
    data class PredictionRequest(
        val weight: String,
        val height: String,
        val ageInMonths: Int
    )

    @Serializable
    data class PredictionResponse(
        val prediction: String
    )

    suspend fun predictRisk(jenisKelamin: Int, height: BigDecimal, ageInMonths: Int): String {
        val connection = URL(apiURL).openConnection() as HttpURLConnection
        return try {
            connection.apply {
                requestMethod = "POST"
                doOutput = true
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                setRequestProperty("Accept", "application/json")
                connectTimeout = 5000
                readTimeout = 5000
            }

            // Create JSON request body matching the API's expected format
            val jsonRequest = """
            {
                "Umur (bulan)": $ageInMonths,
                "Jenis Kelamin": $jenisKelamin,
                "Tinggi Badan (cm)": ${height.toPlainString()}
            }
        """.trimIndent()

            // Send request
            connection.outputStream.use { output ->
                output.write(jsonRequest.toByteArray(Charsets.UTF_8))
                output.flush()
            }

            // Handle response
            if (connection.responseCode !in 200..299) {
                val errorResponse = try {
                    connection.errorStream?.use { it.bufferedReader().readText() } ?: "No error details"
                } catch (e: Exception) {
                    "Failed to read error response: ${e.message}"
                }
                throw RuntimeException("""
                Prediction API request failed with status ${connection.responseCode}.
                Request: $jsonRequest
                Response: $errorResponse
            """.trimIndent())
            }

            val response = connection.inputStream.use { input ->
                input.bufferedReader().use { it.readText() }
            }

            // Parse the response - adjust this based on your actual API response format
            return response.takeIf { it.isNotBlank() }
                ?: throw RuntimeException("Empty response from prediction API")

        } catch (e: Exception) {
            throw RuntimeException("Failed to predict stunting risk: ${e.message}", e)
        } finally {
            connection.disconnect()
        }
    }
}