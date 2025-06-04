package com.example.nutrigrow.backend

import com.example.nutrigrow.backend.config.*
import com.example.nutrigrow.backend.routes.configureRoutes
import com.example.nutrigrow.backend.utils.DatabaseSeeder
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        // logger(org.koin.core.logger.PrintLogger(org.koin.core.logger.Level.INFO))
        modules(appModule)
    }
    embeddedServer(Netty, port = 8000, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureDatabases()
    configureRouting()
    configureRoutes()
    DatabaseSeeder.seed()
}