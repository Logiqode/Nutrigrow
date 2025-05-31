package com.example.nutrigrow.backend.routes

import com.example.nutrigrow.backend.models.StuntingDTO
import com.example.nutrigrow.backend.services.StuntingService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.math.BigDecimal

fun Route.stuntingRoutes() {
    val service by inject<StuntingService>()

    route("/stunting") {
        get {
            val userId = call.request.queryParameters["userId"]?.toIntOrNull()
            if (userId != null) {
                call.respond(service.getStuntingRecordsByUser(userId))
            } else {
                call.respond(service.getAllStuntingRecords())
            }
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            service.getStuntingRecordById(id)?.let { call.respond(it) }
                ?: call.respondText("Not found", status = HttpStatusCode.NotFound)
        }

        post {
            val record = call.receive<StuntingDTO>()
            call.respond(service.createStuntingRecord(record))
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            val record = call.receive<StuntingDTO>()
            if (service.updateStuntingRecord(id, record)) {
                call.respondText("Updated successfully")
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            if (service.deleteStuntingRecord(id)) {
                call.respondText("Deleted successfully")
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }

        post("/predict") {
            val params = call.receiveParameters()
            val weight = params["weight"]?.toBigDecimalOrNull()
                ?: return@post call.respondText("Invalid weight", status = HttpStatusCode.BadRequest)
            val height = params["height"]?.toBigDecimalOrNull()
                ?: return@post call.respondText("Invalid height", status = HttpStatusCode.BadRequest)
            val ageInMonths = params["ageInMonths"]?.toIntOrNull()
                ?: return@post call.respondText("Invalid age", status = HttpStatusCode.BadRequest)

            val prediction = service.predictStuntingRisk(weight, height, ageInMonths)
            call.respond(mapOf("prediction" to prediction))
        }
    }
}