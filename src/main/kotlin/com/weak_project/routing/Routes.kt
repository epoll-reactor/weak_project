package com.weak_project.routing

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*
import com.weak_project.repository.UserRepository

fun Routing.loginForm() {
    get("/") {
        call.respondTemplate("src/main/resources/files/login_form.html")
    }
}

fun Routing.login() {
    get("/login") {
        val username = call.parameters["username"]
        val password = call.parameters["password"]

        val repository = UserRepository()

        if (username != null && password != null) {
            repository.register(username, password)
        }
    }
}