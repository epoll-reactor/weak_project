package com.weak_project.routing

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.routing.*

/**
 * Shows main window with log in screen.
 */
fun Routing.installStartScreenRoute() {
    get("/") {
        call.respondTemplate("src/main/resources/files/login_form.html")
    }
}