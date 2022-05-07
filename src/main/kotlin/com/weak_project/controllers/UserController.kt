package com.weak_project.controllers

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.response.*
import io.ktor.sessions.*
import com.weak_project.models.*
import com.weak_project.views.*

/**
 * Create/access account operations handler.
 */
class UserController {
    /**
     * Try to log in into system. On success goes to user profile,
     * on failure shows dialog with error message.
     */
    suspend fun login(call: ApplicationCall) {
        this.username = call.parameters["username"]!!
        this.password = call.parameters["password"]!!

        val user = UserModel.login(this.username, this.password)
        if (user != null) {
            call.sessions.set(user)
            call.respondRedirect("/profile/${username}")
        } else {
            call.respondErrorDialog("Wrong username or password.")
        }
    }

    /**
     * Try to register user. On success goes to account creation form,
     * on failure shows dialog with error message.
     */
    suspend fun register(call: ApplicationCall) {
        this.username = call.parameters["username"]!!
        this.password = call.parameters["password"]!!

        if (UserModel.userExists(this.username)) {
            call.respondErrorDialog("User $username already registered.")
            return
        }

        call.respondRegister()
    }

    suspend fun createAccount(call: ApplicationCall) {
        val firstName = call.parameters["firstName"]!!
        val lastName = call.parameters["lastName"]!!
        val employerOrEmployee = (call.parameters["employerOrEmployee"]!!).toInt()

        UserModel.register(username, password, firstName, lastName, employerOrEmployee)
        call.sessions.set(
            User(
                username = "",
                password = "",
                firstName = "",
                lastName = "",
                country = "",
                city = "",
                birthDate = "",
                gender = 1,
                phone = "",
                employerOrEmployee = employerOrEmployee
            )
        )
        call.respondRedirect("/profile/${username}")
    }

    private var username: String = ""
    private var password: String = ""
}

fun Routing.user(controller: UserController) {
    get("/") { call.respondLogin() }
    get("/login") { controller.login(call) }
    get("/register") { controller.register(call) }
    get ("/create_account") { controller.createAccount(call) }
}