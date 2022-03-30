package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*

/**
 * Dialog view. Used by FreeMarker.
 */
data class Dialog(
    val info: String
)

suspend fun ApplicationCall.respondDialog(message: String) {
    val dialog = Dialog(message)
    respond(
        FreeMarkerContent(
        "src/main/resources/files/dialog.html",
        mapOf("dialog" to dialog), "name")
    )
}