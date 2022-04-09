package com.weak_project.mvc.user

import io.ktor.application.*
import io.ktor.routing.*
import com.weak_project.mvc.profile.respondProfile
import com.weak_project.view.respondDialog

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
            call.respondProfile()
        } else {
            call.respondDialog("Wrong username or password.")
        }
    }

    /**
     * Try to register user. On success goes to account creation form,
     * on failure shows dialog with error message.
     */
    suspend fun register(call: ApplicationCall) {
        this.username = call.parameters["username"]!!
        this.password = call.parameters["password"]!!

        call.respondRegister()
    }

    suspend fun createAccount(call: ApplicationCall) {
        val firstName = call.parameters["firstName"]!!
        val lastName = call.parameters["lastName"]!!

        try {
            UserModel.register(username, password, firstName, lastName)
            call.respondProfile()
        } catch (e: Exception) {
            call.respondDialog("User $username already registered.")
        }
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