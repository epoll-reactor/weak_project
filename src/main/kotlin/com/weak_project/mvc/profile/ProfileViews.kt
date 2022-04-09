package com.weak_project.mvc.profile

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.sessions.*
import com.weak_project.mvc.user.*
import com.weak_project.view.respondDialog

/// TODO: Get rid of this boilerplate.
data class UserView(
    val username: String,
    val password: String,
    var firstName: String,
    var lastName: String,
    var country: String,
    var city: String,
    var birthDate: String,
    var gender: String,
    var phone: String
)

fun toUserView(user: User) = UserView(
    username = user.username,
    password = user.password,
    firstName = user.firstName,
    lastName = user.lastName,
    country = user.country,
    city = user.city,
    birthDate = user.birthDate,
    gender = resolveGenderFromInt(user.gender),
    phone = user.phone
)

fun getSessionUser(call: ApplicationCall): UserView {
    val session = call.sessions.get<UserSession>()!!
    val user = ProfileModel.getByUsername(session.username)
        ?: throw RuntimeException("Username ${session.username} not found")
    return toUserView(user)
}

suspend fun respondUserTemplate(call: ApplicationCall, template: String) {
    try {
        val userView = getSessionUser(call)
        call.respond(
            FreeMarkerContent(
                template,
                mapOf("user" to userView)
            )
        )
    } catch (e: Exception) {
        call.respondDialog(e.message!!)
    }
}

suspend fun ApplicationCall.respondSettings() {
    respondUserTemplate(
        this,
        "src/main/kotlin/com/weak_project/mvc/profile/ProfileSettings.html"
    )
}

suspend fun ApplicationCall.respondProfile() {
    respondUserTemplate(
        this,
        "src/main/kotlin/com/weak_project/mvc/profile/Profile.html"
    )
}
