package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*

suspend fun ApplicationCall.respondLogin() {
    respondTemplate("src/main/resources/templates/LoginForm.html")
}

suspend fun ApplicationCall.respondRegister() {
    respondTemplate("src/main/resources/templates/RegisterForm.html")
}