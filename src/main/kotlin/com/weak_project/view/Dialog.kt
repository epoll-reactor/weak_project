package com.weak_project.view

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*

/**
 * Dialog view. Used by FreeMarker.
 */
data class Dialog(
    val info: String
)

/**
 * Shows page with given message.
 *
 * @TODO floating (pop-up) window.
 */
suspend fun ApplicationCall.respondDialog(message: String) {
    val dialog = Dialog(message)
    respond(
        FreeMarkerContent(
            "src/main/resources/files/Dialog.html",
            mapOf("dialog" to dialog)
        )
    )
}