package com.weak_project.routing

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.loginForm() {
    get("/") {
        call.respondTemplate("src/main/resources/files/login_form.html")
    }
}

fun Routing.login() {
    get("/login") {
        val username = call.parameters["username"]
        val password = call.parameters["password"]
        call.respondText("Okay: $username, $password")
    }
}