package com.weak_project.config

import com.weak_project.controllers.*
import com.weak_project.routing.*
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

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
        module = Application::mainModule
    )

fun Application.setupRoutes() {
    val userController = UserController()

    routing {
        installStartScreenRoute()
        installUserRoutes(userController)
    }
}

fun Application.mainModule() {
    install(FreeMarker)
    setupRoutes()
}