package com.example.nutrigrow.backend.routes

import com.example.nutrigrow.backend.models.StuntingDTO
import com.example.nutrigrow.backend.services.StuntingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.math.BigDecimal

fun Route.stuntingRoutes() {
    val service by inject<StuntingService>()

    route("/stunting") {
        // Get all records or filter by user
        get {
            try {
                val userId = call.request.queryParameters["userId"]?.toIntOrNull()
                val records = if (userId != null) {
                    service.getStuntingRecordsByUser(userId)
                } else {
                    service.getAllStuntingRecords()
                }
                call.respond(HttpStatusCode.OK, records)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to retrieve records: ${e.message}")
                )
            }
        }

        // Get single record by ID
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid ID format")
                )

            try {
                service.getStuntingRecordById(id)?.let { record ->
                    call.respond(HttpStatusCode.OK, record)
                } ?: call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Record not found")
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to retrieve record: ${e.message}")
                )
            }
        }

        // Create new record
        post {
            try {
                val record = call.receive<StuntingDTO>()
                val createdRecord = service.createStuntingRecord(record)
                call.respond(HttpStatusCode.Created, createdRecord)
            } catch (e: ContentTransformationException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid request body format")
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to create record: ${e.message}")
                )
            }
        }

        // Update existing record
        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid ID format")
                )

            try {
                val record = call.receive<StuntingDTO>()
                if (service.updateStuntingRecord(id, record)) {
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Record updated successfully"))
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        mapOf("error" to "Record not found")
                    )
                }
            } catch (e: ContentTransformationException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid request body format")
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to update record: ${e.message}")
                )
            }
        }

        // Delete record
        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid ID format")
                )

            try {
                if (service.deleteStuntingRecord(id)) {
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Record deleted successfully"))
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        mapOf("error" to "Record not found")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to delete record: ${e.message}")
                )
            }
        }

        // Predict stunting risk
        post("/predict") {
            try {
                val params = call.receiveParameters()

                val jenisKelamin = params["jenisKelamin"]?.toIntOrNull()
                    ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "Missing or invalid jenisKelamin parameter (0 or 1)")
                    )

                val height = params["height"]?.toBigDecimalOrNull()
                    ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "Missing or invalid height parameter")
                    )

                val ageInMonths = params["ageInMonths"]?.toIntOrNull()
                    ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "Missing or invalid ageInMonths parameter")
                    )

                val prediction = service.predictStuntingRisk(jenisKelamin, height, ageInMonths)
                call.respond(HttpStatusCode.OK, mapOf("prediction" to prediction))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Prediction failed: ${e.message}")
                )
            }
        }

    }
}