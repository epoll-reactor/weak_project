package com.weak_project.config

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.weak_project.routing.*

fun createApplication(): BaseApplicationEngine {
    setupDatabaseServer()
    return createServer(Netty)
}

fun createServer(
    engine: ApplicationEngineFactory<BaseApplicationEngine,
            out ApplicationEngine.Configuration>
): BaseApplicationEngine {
    return embeddedServer(
        engine,
        port = 8080,
        module = Application::mainModule
    )
}

fun Application.setupRoutes() {
    routing {
        loginForm()
        login()
    }
}

fun Application.mainModule() {
    install(FreeMarker)
    setupRoutes()
}