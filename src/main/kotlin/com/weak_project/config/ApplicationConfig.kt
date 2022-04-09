package com.weak_project.config

import com.weak_project.mvc.profile.ProfileController
import com.weak_project.mvc.profile.profile
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.routing.*
import com.weak_project.mvc.user.UserController
import com.weak_project.mvc.user.user

fun Application.setupRoutes() {
    val userController = UserController()
    val profileController = ProfileController()

    routing {
        user(userController)
        profile(profileController)
    }
}

@Suppress("unused")
fun Application.module() {
    install(FreeMarker)
    setupDatabaseServer(environment.config)
    setupRoutes()
}