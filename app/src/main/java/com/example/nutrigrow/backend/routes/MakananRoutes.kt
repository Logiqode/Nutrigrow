package com.example.nutrigrow.backend.routes

import com.example.nutrigrow.backend.models.MakananBahanDTO
import com.example.nutrigrow.backend.models.MakananDTO
import com.example.nutrigrow.backend.services.MakananService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.makananRoutes() {
    val service by inject<MakananService>()

    route("/makanan") {
        get {
            call.respond(service.getAllMakanan())
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            service.getMakananById(id)?.let { call.respond(it) }
                ?: call.respondText("Not found", status = HttpStatusCode.NotFound)
        }

        post {
            val makanan = call.receive<MakananDTO>()
            call.respond(service.createMakanan(makanan))
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            val makanan = call.receive<MakananDTO>()
            if (service.updateMakanan(id, makanan)) {
                call.respondText("Updated successfully")
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            if (service.deleteMakanan(id)) {
                call.respondText("Deleted successfully")
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }

        route("/{id}/bahan") {
            get {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                call.respond(service.getBahanForMakanan(id))
            }

            post {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@post call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val bahan = call.receive<MakananBahanDTO>().copy(makananId = id)
                if (service.addBahanToMakanan(bahan)) {
                    call.respondText("Added successfully")
                } else {
                    call.respondText("Failed to add", status = HttpStatusCode.BadRequest)
                }
            }

            delete {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val bahan = call.receive<MakananBahanDTO>().copy(makananId = id)
                if (service.removeBahanFromMakanan(bahan)) {
                    call.respondText("Removed successfully")
                } else {
                    call.respondText("Failed to remove", status = HttpStatusCode.BadRequest)
                }
            }
        }
    }
}