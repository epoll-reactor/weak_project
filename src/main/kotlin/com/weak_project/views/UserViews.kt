package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*

suspend fun ApplicationCall.respondLogin() {
    respondTemplate("src/main/resources/templates/Forms/LoginForm.html")
}

suspend fun ApplicationCall.respondRegister() {
    respondTemplate("src/main/resources/templates/Forms/RegisterForm.html")
}