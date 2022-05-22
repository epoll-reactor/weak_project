package com.weak_project.config

import io.ktor.util.*
import io.ktor.config.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

@OptIn(KtorExperimentalAPI::class)
fun setupDatabaseServer(config: ApplicationConfig) {
    val databaseConfig = config.config("ktor.database")

    fun get(property: String) =
        databaseConfig.property(property).getString()

    val ds = HikariDataSource(HikariConfig().apply {
        jdbcUrl = get("jdbcUrl")
        username = get("username")
        password = get("password")
        driverClassName = get("driver")
    })
    Database.connect(ds)
}