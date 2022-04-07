package com.weak_project.config

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.routing.*
import com.weak_project.mvc.user.UserController
import com.weak_project.mvc.user.installUserRoutes

fun Application.setupRoutes() {
    val userController = UserController()

    routing {
        installUserRoutes(userController)
    }
}

/// This is called through config specification.
@Suppress("unused")
fun Application.module() {
    install(FreeMarker)
    install(Authentication)
    setupDatabaseServer(environment.config)
    setupRoutes()
}