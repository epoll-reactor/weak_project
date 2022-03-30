package com.weak_project.routing

import com.weak_project.controllers.UserController
import io.ktor.application.*
import io.ktor.routing.*

fun Routing.installUserRoutes(controller: UserController) {
    get("/login") { controller.login(call) }
    get("/register") { controller.register(call) }
    get ("/create_account") { controller.createAccount(call) }
}
