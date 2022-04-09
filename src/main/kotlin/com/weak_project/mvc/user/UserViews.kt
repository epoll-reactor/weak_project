package com.weak_project.mvc.user

import io.ktor.application.*
import io.ktor.freemarker.*

suspend fun ApplicationCall.respondLogin() {
    respondTemplate("src/main/kotlin/com/weak_project/mvc/user/LoginForm.html")
}

suspend fun ApplicationCall.respondRegister() {
    respondTemplate("src/main/kotlin/com/weak_project/mvc/user/RegisterForm.html")
}