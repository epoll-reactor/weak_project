package com.weak_project.controllers

import com.weak_project.models.MessagesModel
import com.weak_project.models.User
import com.weak_project.views.*
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*

class MessagesController {
    suspend fun respondMessagesList(call: ApplicationCall) {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        call.respondMessagesList(
            MessagesModel.getDialogList(id)
        )
    }

    suspend fun respondPrivateDialog(call: ApplicationCall) {
        val session = call.sessions.get<User>()
        if (session == null) {
            call.respondLogin()
            return
        }
        val user1 = session.id
        val user2 = call.parameters.getOrFail<Int>("id").toInt()

        call.respondPrivateDialog(user1, user2)
    }

    suspend fun sendMessage(call: ApplicationCall) {
        val session = call.sessions.get<User>()
        if (session == null) {
            call.respondLogin()
            return
        }

        val toId = call.parameters.getOrFail<Int>("id").toInt()
        val text = call.parameters["messageText"]!!

        MessagesModel.insert(
            text_ = text,
            from_ = session.id,
            to_ = toId,
            timestamp_ = System.currentTimeMillis() / 1000
        )

        respondPrivateDialog(call)
    }
}

fun Routing.messages(controller: MessagesController) {
    get("/messages/id{id}") { controller.respondMessagesList(call) }
    get("/dialog/id{id}") { controller.respondPrivateDialog(call) }
    get("/sendMessage/to{id}") { controller.sendMessage(call) }
}