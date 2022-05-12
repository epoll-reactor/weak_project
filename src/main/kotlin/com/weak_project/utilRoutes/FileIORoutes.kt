package com.weak_project.utilRoutes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

// Dead code at the moment.
fun Routing.fileIO() {
    post("/upload") { _ ->
        call.receiveMultipart().forEachPart { part ->
            if (part is PartData.FileItem) {
                val name = part.originalFileName!!
                val file = File("$name")

                part.streamProvider().use { its ->
                    file.outputStream().buffered().use {
                        its.copyTo(it)
                    }
                }
            }

            part.dispose()
        }
    }
    // File download.
    get("/{filename}") {
        val filename = call.parameters["filename"]!!
        println("Downloading $filename")
        val file = File("$filename")
        if (file.exists())
            call.respondFile(file)
        call.respond(HttpStatusCode.NotFound)
    }
}