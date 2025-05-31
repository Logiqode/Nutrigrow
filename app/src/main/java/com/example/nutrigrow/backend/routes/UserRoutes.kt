package com.example.nutrigrow.backend.routes

import com.example.nutrigrow.backend.models.UserCredentialDTO
import com.example.nutrigrow.backend.models.UserDTO
import com.example.nutrigrow.backend.services.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val service by inject<UserService>()

    route("/user") {
        get {
            call.respond(service.getAllUsers())
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            service.getUserById(id)?.let { call.respond(it) }
                ?: call.respondText("Not found", status = HttpStatusCode.NotFound)
        }

        post {
            val user = call.receive<UserDTO>()
            call.respond(service.createUser(user))
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            val user = call.receive<UserDTO>()
            if (service.updateUser(id, user)) {
                call.respondText("Updated successfully")
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            if (service.deleteUser(id)) {
                call.respondText("Deleted successfully")
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }

        post("/register") {
            val registerData = call.receive<RegisterRequest>()
            val credential = UserCredentialDTO(
                email = registerData.email,
                password = registerData.password,
                username = registerData.username,
                userId = 0 // Will be set by service
            )
            val user = UserDTO(
                name = registerData.name,
                address = registerData.address,
                phone = registerData.phone,
                gender = registerData.gender
            )
            val result = service.registerUser(credential, user)
            call.respond(result)
        }

        post("/login") {
            val loginData = call.receive<LoginRequest>()
            val credential = service.login(loginData.username, loginData.password)
            if (credential != null) {
                call.respond(credential)
            } else {
                call.respondText("Invalid credentials", status = HttpStatusCode.Unauthorized)
            }
        }
    }
}

data class RegisterRequest(
    val email: String,
    val password: String,
    val username: String,
    val name: String,
    val address: String,
    val phone: String,
    val gender: Char
)

data class LoginRequest(
    val username: String,
    val password: String
)