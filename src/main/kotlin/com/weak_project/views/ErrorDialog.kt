package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*

/**
 * Dialog view. Used by FreeMarker.
 */
data class Dialog(val info: String)

/**
 * Shows page with given message.
 *
 * @TODO floating (pop-up) window.
 */
suspend fun ApplicationCall.respondErrorDialog(message: String) {
    val dialog = Dialog(message)
    respond(
        FreeMarkerContent(
            "src/main/resources/templates/ErrorDialog.html",
            mapOf("dialog" to dialog)
        )
    )
}