package com.example.nutrigrow.backend.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureRoutes() {
    routing {
        bahanMakananRoutes()
        makananRoutes()
        beritaRoutes()
        stuntingRoutes()
        userRoutes()
    }
}