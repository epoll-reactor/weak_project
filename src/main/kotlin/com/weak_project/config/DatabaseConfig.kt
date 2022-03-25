package com.weak_project.config

import org.jetbrains.exposed.sql.Database

fun setupDatabaseServer() {
    Database.connect(
        "jdbc:h2:mem:test",
        driver = "org.h2.Driver",
        user = "root",
        password = ""
    )
}