package com.weak_project.config

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.weak_project.mvc.user.UserController
import com.weak_project.mvc.user.installUserRoutes

/**
 * Configure all modules and create server.
 */
fun createApplication(): BaseApplicationEngine {
    setupDatabaseServer()
    return createServer(Netty)
}

fun createServer(
    engine: ApplicationEngineFactory<BaseApplicationEngine,
            out ApplicationEngine.Configuration>
) = embeddedServer(
        engine,
        port = 8080,
        module = Application::mainModule,
        watchPaths = listOf("classes")
    )

fun Application.setupRoutes() {
    val userController = UserController()

    routing {
        installUserRoutes(userController)
    }
}

fun Application.mainModule() {
    install(FreeMarker)
    install(Authentication)
    setupRoutes()
}