package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*

/**
 * Shows page with given message.
 *
 * @TODO floating (pop-up) window.
 */
suspend fun ApplicationCall.respondErrorDialog(message: String) {
    respond(
        FreeMarkerContent(
            "src/main/resources/templates/Dialogs/ErrorDialog.html",
            mapOf("dialog" to message)
        )
    )
}