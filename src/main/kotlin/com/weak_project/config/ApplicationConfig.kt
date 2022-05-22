package com.weak_project.config

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.freemarker.*
import io.ktor.routing.*
import io.ktor.sessions.*
import com.weak_project.controllers.*
import com.weak_project.controllers.user
import com.weak_project.models.User

fun Application.setupRoutes() {
    val userController = UserController()
    val profileController = ProfileController()
    val employerController = EmployerController()
    val messagesController = MessagesController()
    val jobController = JobController()

    routing {
        static("/static") {
            resources("files")
            resources("templates")
        }

        user(userController)
        profile(profileController)
        employer(employerController)
        messages(messagesController)
        jobs(jobController)
    }
}

@Suppress("unused")
fun Application.module() {
    install(FreeMarker)
    install(Sessions) {
        cookie<User>("user")
    }
    setupDatabaseServer(environment.config)
    setupRoutes()
}