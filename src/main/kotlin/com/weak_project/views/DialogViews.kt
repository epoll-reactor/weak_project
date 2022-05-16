package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import com.weak_project.models.MessagesModel
import com.weak_project.models.UserModel

internal fun makeDialogPath(template: String) = "src/main/resources/templates/Messages/$template.html"

suspend fun ApplicationCall.respondPrivateDialog(user1: Int, user2: Int) {
    val messages = MessagesModel.getPrivateDialog(user1, user2)
    val companion = UserModel.getById(user2)

    respond(
        FreeMarkerContent(
            makeDialogPath("Dialog"),
            mapOf("messages" to messages, "companion" to "${companion.firstName} ${companion.lastName}")
        )
    )
}