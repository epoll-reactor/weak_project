package com.weak_project.config

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.freemarker.*
import io.ktor.routing.*
import io.ktor.sessions.*
import com.weak_project.controllers.*
import com.weak_project.controllers.user
import com.weak_project.sessions.UserSession

fun Application.setupRoutes() {
    val userController = UserController()
    val profileController = ProfileController()

    routing {
        static("/static") {
            resources("files")
            resources("templates")
        }

        user(userController)
        profile(profileController)
    }
}

@Suppress("unused")
fun Application.module() {
    install(FreeMarker)
    install(Sessions) {
        cookie<UserSession>("user")
    }
    setupDatabaseServer(environment.config)
    setupRoutes()
}