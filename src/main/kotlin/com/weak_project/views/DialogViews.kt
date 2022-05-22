package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import com.weak_project.models.MessagesModel
import com.weak_project.models.User
import com.weak_project.models.UserModel
import io.ktor.sessions.*
import java.text.SimpleDateFormat
import java.util.*

internal fun parseTimestamp(timestamp: Long): String {
    val format = "dd MMM yyyy HH:mm:ss"
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(Date(timestamp * 1000))
}

internal fun makeDialogPath(template: String) = "src/main/resources/templates/Messages/$template.html"

/**
 * Show dialog between two users.
 */
suspend fun ApplicationCall.respondPrivateDialog(user1: Int, user2: Int) {
    val messages = MessagesModel.getPrivateDialog(user1, user2)
    val companion = UserModel.getById(user2)

    val views = toMessageViewList(MessageViewRequest.PRIVATE_DIALOG, messages)

    respond(
        FreeMarkerContent(
            makeDialogPath("Dialog"),
            mapOf(
                "messageViews" to views,
                "companion" to "${companion.firstName} ${companion.lastName}",
                "companionId" to companion.id
            )
        )
    )
}