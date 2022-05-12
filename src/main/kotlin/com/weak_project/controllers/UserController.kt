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
            call.respondRedirect("/profile/id${user.id}")
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

        val id = UserModel.register(username, password, firstName, lastName, employerOrEmployee)
        call.sessions.set(
            User(
                id = id,
                username = username,
                password = "",
                firstName = firstName,
                lastName = lastName,
                country = "",
                city = "",
                birthDate = "",
                gender = 1,
                phone = "",
                employerOrEmployee = employerOrEmployee
            )
        )
        call.respondRedirect("/profile/id${id}")
    }

    suspend fun updateAvatar(call: ApplicationCall) {
        val session = call.sessions.get<User>()
        if (session == null) {
            call.respondErrorDialog("Session does not exist or is expired.")
            return
        }

        UserModel.updateAvatar(session.username, "/home/machen/media/pics/BASH_COLORS.png")

        call.respondRedirect("/profile/id${session.id}")
    }

    private var username: String = ""
    private var password: String = ""
}

fun Routing.user(controller: UserController) {
    get("/") { call.respondLogin() }
    get("/login") { controller.login(call) }
    get("/register") { controller.register(call) }
    get ("/create_account") { controller.createAccount(call) }
    get("/update_avatar") { controller.updateAvatar(call) }
}