package com.example.nutrigrow.backend.routes

import com.example.nutrigrow.backend.models.BeritaDTO
import com.example.nutrigrow.backend.services.BeritaService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.beritaRoutes() {
    val service by inject<BeritaService>()

    route("/berita") {
        get {
            val categoryId = call.request.queryParameters["categoryId"]?.toIntOrNull()
            if (categoryId != null) {
                call.respond(service.getBeritaByCategory(categoryId))
            } else {
                call.respond(service.getAllBerita())
            }
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            service.getBeritaById(id)?.let { call.respond(it) }
                ?: call.respondText("Not found", status = HttpStatusCode.NotFound)
        }

        post {
            val berita = call.receive<BeritaDTO>()
            call.respond(service.createBerita(berita))
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            val berita = call.receive<BeritaDTO>()
            if (service.updateBerita(id, berita)) {
                call.respondText("Updated successfully")
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            if (service.deleteBerita(id)) {
                call.respondText("Deleted successfully")
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}