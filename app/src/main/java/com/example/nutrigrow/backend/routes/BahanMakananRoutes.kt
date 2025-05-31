package com.example.nutrigrow.backend.routes

import com.example.nutrigrow.backend.models.BahanMakananDTO
import com.example.nutrigrow.backend.services.BahanMakananService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.bahanMakananRoutes() {
    val service by inject<BahanMakananService>()

    route("/bahan-makanan") {
        get {
            call.respond(service.getAllBahanMakanan())
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            service.getBahanMakananById(id)?.let { call.respond(it) }
                ?: call.respondText("Not found", status = HttpStatusCode.NotFound)
        }

        post {
            val bahan = call.receive<BahanMakananDTO>()
            call.respond(service.createBahanMakanan(bahan))
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            val bahan = call.receive<BahanMakananDTO>()
            if (service.updateBahanMakanan(id, bahan)) {
                call.respondText("Updated successfully")
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            if (service.deleteBahanMakanan(id)) {
                call.respondText("Deleted successfully")
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}