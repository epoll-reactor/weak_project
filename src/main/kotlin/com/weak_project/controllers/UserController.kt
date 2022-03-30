package com.weak_project.controllers

import com.weak_project.repository.UserRepository
import com.weak_project.views.respondDialog
import io.ktor.application.*
import io.ktor.freemarker.*

/**
 * Create/access account operations handler.
 */
class UserController {
    /**
     * Try to log in into system. On success goes to user profile,
     * on failure shows dialog with error message.
     */
    suspend fun login(call: ApplicationCall) {
        val username = call.parameters["username"]!!
        val password = call.parameters["password"]!!

        val user = UserRepository.login(username, password)
        if (user != null) {
            call.respondDialog("Here should be user profile")
        } else {
            call.respondDialog("Wrong username or password")
        }
    }

    /**
     * Try to register user. On success goes to account creation form,
     * on failure shows dialog with error message.
     */
    suspend fun register(call: ApplicationCall) {
        val username = call.parameters["username"]!!
        val password = call.parameters["password"]!!

        try {
            UserRepository.register(username, password)
            call.respondTemplate("src/main/resources/files/registration_form.html")
        } catch (e: Exception) {
            call.respondDialog("User $username already registered")
        }
    }

    suspend fun createAccount(call: ApplicationCall) {
        val firstName = call.parameters["first_name"]
        val secondName = call.parameters["second_name"]

        call.respondDialog("Hello, $firstName $secondName")
    }
}