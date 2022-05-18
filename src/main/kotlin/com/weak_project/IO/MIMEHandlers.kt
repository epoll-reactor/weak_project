package com.weak_project.IO

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import java.io.File

suspend fun uploadFile(call: ApplicationCall, targetFilename: String): String {
    var targetFile = ""

    call.receiveMultipart().forEachPart { part ->
        if (part is PartData.FileItem) {
            val ext = File(part.originalFileName!!).extension
            targetFile = "$targetFilename.$ext"
            println("Target file: $targetFile")
            val file = File("src/main/resources/files/", targetFile)
            part.streamProvider().use { its -> file.outputStream().buffered().use { its.copyTo(it) } }
        }

        part.dispose
    }

    return "src/main/resources/files/$targetFile"
}