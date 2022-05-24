package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import com.weak_project.models.Message
import com.weak_project.models.User
import com.weak_project.models.UserModel
import io.ktor.sessions.*
import java.io.File

data class MessageView(
    val text: String,
    val from: String,
    val to: String,
    val toId: Int,
    val timestamp: String,
    var avatarPath: String
)

enum class MessageViewRequest {
    /// This used to respond avatars of two persons.
    PRIVATE_DIALOG,
    /// This used to respond only sender avatar.
    DIALOG_LIST
}

/**
 * Create the views for private dialog (with participant) or dialog list.
 */
fun toMessageViewList(requestType: MessageViewRequest, messages: MutableList<Message>): MutableList<MessageView> {
    val views = mutableListOf<MessageView>()

    messages.forEach { message ->
        val fromUser = UserModel.getById(message.from)
        val toUser = UserModel.getById(message.to)

        val view = MessageView(
            text = message.text,
            from = fromUser.username,
            to = toUser.firstName + " " + toUser.lastName,
            toId = toUser.id,
            timestamp = parseTimestamp(message.timestamp),
            avatarPath = ""
        )

        val avatarId = if (requestType == MessageViewRequest.DIALOG_LIST) {
            message.to
        } else {
            message.from
        }

        val avatar = UserModel.getAvatar(avatarId)
        if (avatar != null) {
            view.avatarPath = "/static/avatar$avatarId.png"
            val realAvatarPath = "build/resources/main/files/avatar$avatarId.png"
            val file = File(realAvatarPath)
            if (!file.exists()) {
                file.writeBytes(avatar)
            }
        } else {
            view.avatarPath = "/static/NoAvatar.png"
        }

        views += view
    }

    return views
}

internal fun makeMessagesPath(template: String) = "src/main/resources/templates/Messages/$template.html"

suspend fun ApplicationCall.respondMessagesList(messages: MutableList<Message>) {
    val views = toMessageViewList(MessageViewRequest.DIALOG_LIST, messages)

    val session = sessions.get<User>()
    if (session == null) {
        respondLogin()
        return
    }

    respond(
        FreeMarkerContent(
            makeMessagesPath("MessagesList"),
            mapOf(
                "messagesViews" to views,
                "userId" to session.id
            )
        )
    )
}