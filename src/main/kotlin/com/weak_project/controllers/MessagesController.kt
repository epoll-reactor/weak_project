package com.weak_project.controllers

import com.weak_project.models.MessagesModel
import com.weak_project.models.User
import com.weak_project.views.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*

class MessagesController {
}

fun Routing.messages(controller: MessagesController) {
    get("/messages/id{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        call.respondMessagesList(
            MessagesModel.getDialogList(id)
        )
    }
    get("/dialog/id{id}") {
        val session = call.sessions.get<User>()
        if (session == null) {
            call.respondErrorDialog("Session does not exist or is expired.")
            return@get
        }
        val user1 = session.id
        val user2 = call.parameters.getOrFail<Int>("id").toInt()

        call.respondPrivateDialog(user1, user2)
    }
}