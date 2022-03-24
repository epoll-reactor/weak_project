package com.weak_project

import io.ktor.server.engine.*
import com.weak_project.config.createApplication

@OptIn(EngineAPI::class)
fun main() {
    createApplication().start(wait = true)
}