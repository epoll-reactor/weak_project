package com.weak_project.routing.login

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.routing.*

fun Routing.loginForm() {
    get("/") {
        call.respondTemplate("src/main/resources/files/login_form.html")
    }
}