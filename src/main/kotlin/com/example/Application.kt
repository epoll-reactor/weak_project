package com.example

import io.ktor.freemarker.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            static("/static") {
                resources("files")
            }
            get ("/") {
                call.respondText("Bonnie & Clyde")
            }
        }
    }.start(wait = true)
}
