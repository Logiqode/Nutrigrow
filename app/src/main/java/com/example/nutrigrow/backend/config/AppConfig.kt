package com.example.nutrigrow.backend.config

// Import your DatabaseConfig object
import com.example.nutrigrow.backend.models.*
import com.example.nutrigrow.backend.repositories.*
import com.example.nutrigrow.backend.services.*
import com.example.nutrigrow.backend.utils.PasswordHasher
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.slf4j.event.Level

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    log.info("Serialization configured with Kotlinx.JSON.")
}

fun Application.configureDatabases() {
    // Use the imported DatabaseConfig object
    val dbUrl = DatabaseConfig.url
    val dbDriver = DatabaseConfig.driver
    val dbUser = DatabaseConfig.user
    val dbPassword = DatabaseConfig.password

    log.info("Attempting to connect to database: URL=$dbUrl, User=$dbUser, Driver=$dbDriver, Password=$dbPassword")
    // Avoid logging password in production environments

    try {
        Database.connect(
            url = dbUrl,
            driver = dbDriver,
            user = dbUser,
            password = dbPassword
        )
        log.info("Successfully connected to the database.")

        transaction {
            // Create tables if they don't exist
            SchemaUtils.create(
                Users,
                UserCredentials,
                BahanMakanans,
                KategoriBeritas,
                Beritas,
                Makanans,
                MakananBahan,
                Stunting
            )
            log.info("Database tables checked/created.")
        }
    } catch (e: Exception) {
        log.error("Failed to connect to the database or create tables.", e)
        // Depending on your application's needs, you might want to re-throw or exit
        throw e // Re-throw to let Ktor handle startup failure
    }
    log.info("Database configuration process complete.")
}

val appModule = module {
    // Repositories
    single { BahanMakananRepository() }
    single { BeritaRepository() }
    single { MakananRepository() }
    single { StuntingRepository() }
    single { UserRepository() }
    single { KategoriBeritaRepository() }

    // Services - assuming constructor injection
    single { BahanMakananService() }
    single { BeritaService() }
    single { MakananService() }
    single { StuntingService() }
    single { UserService() }

    // Utilities
    single { PasswordHasher }
}

fun Application.configureKoin() {
    install(Koin) {
        modules(appModule)
    }
    log.info("Koin DI configured.")
}

fun Application.configureRouting() { // Renamed for clarity, often called configureRouting or similar
    install(CallLogging) {
        level = Level.INFO
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled_Exception", cause)
            call.respondText(text = "500: ${cause.message ?: "Internal Server Error"}", status = HttpStatusCode.InternalServerError)
        }
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(text = "404: Page Not Found", status = status)
        }
    }
    log.info("General routing plugins (CallLogging, StatusPages) configured.")
    // Specific application routes would be configured elsewhere
    // e.g., in a separate routing.kt file or package.
}
