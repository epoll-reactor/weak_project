package com.weak_project.config

import io.ktor.util.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

@OptIn(KtorExperimentalAPI::class)
fun setupDatabaseServer() {
    val ds = HikariDataSource(HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        username = "exposed"
        password = "exposed"
        driverClassName = "org.postgresql.Driver"
    })
    Database.connect(ds)
}