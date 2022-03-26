package com.weak_project.routing.login

import com.weak_project.repository.UserRepository
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.login() {
    get("/login") {
        val username = call.parameters["username"]!!
        val password = call.parameters["password"]!!

        val user = UserRepository.login(username, password)
        if (user != null) {
            call.respondText("Logged in")
        } else {
            call.respondText("User $username not found")
        }
    }

    get("/register") {
        val username = call.parameters["username"]!!
        val password = call.parameters["password"]!!

        try {
            UserRepository.register(username, password)
        } catch (e: Exception) {
            call.respondText(e.message!!)
        }
        call.respondTemplate("src/main/resources/files/registration_form.html")
    }

    get ("/create_account") {
        val first_name = call.parameters["first_name"]
        val second_name = call.parameters["second_name"]

        call.respondText("Hello, $first_name $second_name")
    }
}