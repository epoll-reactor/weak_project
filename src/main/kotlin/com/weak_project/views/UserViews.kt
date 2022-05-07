package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*

internal fun makeFormsPath(template: String) = "src/main/resources/templates/Forms/$template.html"

suspend fun ApplicationCall.respondLogin() {
    respondTemplate(makeFormsPath("LoginForm"))
}

suspend fun ApplicationCall.respondRegister() {
    respondTemplate(makeFormsPath("RegisterForm"))
}