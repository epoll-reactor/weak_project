package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import com.weak_project.models.Message
import com.weak_project.models.UserModel
import java.io.File

data class MessageView(
    val text: String,
    val from: String,
    val to: String,
    val timestamp: Long,
    var avatarPath: String
)

internal fun makeMessagesPath(template: String) = "src/main/resources/templates/Messages/$template.html"

suspend fun ApplicationCall.respondMessagesList(messages: MutableList<Message>) {
    val views = mutableListOf<MessageView>()

   messages.forEach { message ->
       val fromUser = UserModel.getById(message.from)
       val toUser = UserModel.getById(message.to)

       var view = MessageView(
           text = message.text,
           from = fromUser.username,
           to = toUser.firstName + " " + toUser.lastName,
           timestamp = message.timestamp,
           avatarPath = ""
       )

       val avatar = UserModel.getAvatar(message.to)
       if (avatar != null) {
           view.avatarPath = "/static/avatar${message.to}.png"
           val realAvatarPath = "src/main/resources/files/avatar${message.to}.png"
           val file = File(realAvatarPath)
           if (!file.exists()) {
               file.writeBytes(avatar)
           }
       } else {
           view.avatarPath = "/static/NoAvatar.png"
       }

       views += view
   }

    respond(
        FreeMarkerContent(
            makeMessagesPath("MessagesList"),
            mapOf("messagesViews" to views)
        )
    )
}