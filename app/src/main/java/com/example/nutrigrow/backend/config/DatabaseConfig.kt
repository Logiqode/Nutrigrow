package com.example.nutrigrow.backend.config

import java.io.FileInputStream
import java.util.Properties

object DatabaseConfig {
    private val props = Properties().apply {
        try {
            load(FileInputStream(".env"))
        } catch (e: Exception) {
            setProperty("DB_URL", "jdbc:postgresql://localhost:5432/hci_db")
            setProperty("DB_DRIVER", "org.postgresql.Driver")
            setProperty("DB_USER", "postgres")
            setProperty("DB_PASSWORD", "")
        }
    }

    val url: String = props.getProperty("DB_URL")
    val driver: String = props.getProperty("DB_DRIVER")
    val user: String = props.getProperty("DB_USER")
    val password: String = props.getProperty("DB_PASSWORD")
}