package com.weak_project.routing.login

import com.weak_project.repository.UserRepository
import com.weak_project.views.respondDialog
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.routing.*

fun Routing.loginRoute() {
    get("/login") {
        val username = call.parameters["username"]!!
        val password = call.parameters["password"]!!

        val user = UserRepository.login(username, password)
        if (user != null) {
            call.respondDialog("Here should be user profile")
        } else {
            call.respondDialog("Wrong username or password")
        }
    }
}

fun Routing.registerRoute() {
    get("/register") {
        val username = call.parameters["username"]!!
        val password = call.parameters["password"]!!

        try {
            UserRepository.register(username, password)
        } catch (e: Exception) {
            call.respondDialog("User $username already registered")
        }
        call.respondTemplate("src/main/resources/files/registration_form.html")
    }

    get ("/create_account") {
        val firstName = call.parameters["first_name"]
        val secondName = call.parameters["second_name"]

        call.respondDialog("Hello, $firstName $secondName")
    }
}