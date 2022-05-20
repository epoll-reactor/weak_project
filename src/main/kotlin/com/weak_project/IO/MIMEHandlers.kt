package com.weak_project.IO

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import java.io.File

/**
 * Save file MIME part(s) to disk. All other part types are ignored.
 */
suspend fun uploadFile(call: ApplicationCall, targetFilename: String): String {
    var targetFile = ""

    call.receiveMultipart().forEachPart { part ->
        if (part is PartData.FileItem) {
            val ext = File(part.originalFileName!!).extension
            targetFile = "$targetFilename.$ext"
            val file = File("build/resources/main/files/", targetFile)
            part.streamProvider().use { its -> file.outputStream().buffered().use { its.copyTo(it) } }
        }

        part.dispose
    }

    return "build/resources/main/files/$targetFile"
}