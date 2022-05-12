package com.weak_project.views

import java.io.File
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.sessions.*
import com.weak_project.controllers.resolveGenderFromInt
import com.weak_project.models.*

data class UserView(
    val id: Int,
    val username: String,
    val password: String,
    var firstName: String,
    var lastName: String,
    var country: String,
    var city: String,
    var birthDate: String,
    var gender: String,
    var phone: String,
    var avatarPath: String
)

fun toUserView(user: User) = UserView(
    id = user.id,
    username = user.username,
    password = user.password,
    firstName = user.firstName,
    lastName = user.lastName,
    country = user.country,
    city = user.city,
    birthDate = user.birthDate,
    gender = resolveGenderFromInt(user.gender),
    phone = user.phone,
    avatarPath = ""
)

internal suspend fun getSessionUser(call: ApplicationCall): UserView? {
    val session = call.sessions.get<User>()
    if (session == null) {
        call.respondErrorDialog("Session not exists or expired")
        return null
    }
    // Get full info about user.
    val user = ProfileModel.getByUsername(session.username)
        ?: throw RuntimeException("Username ${session.username} not found.")
    return toUserView(user)
}

internal fun resolveAvatar(view: UserView) {
    val avatar = UserModel.getAvatar(view.username)
    if (avatar != null) {
        view.avatarPath = "/static/avatar${view.id}.png"
        val realAvatarPath = "src/main/resources/files/avatar${view.id}.png"
        val file = File(realAvatarPath)
        if (!file.exists()) {
            file.writeBytes(avatar)
        }
    } else {
        view.avatarPath = "/static/NoAvatar.png"
    }
}

internal suspend fun getUserView(call: ApplicationCall, user: User): UserView? {
    val session = call.sessions.get<User>()
    if (session == null) {
        call.respondErrorDialog("Session not exists or expired")
        return null
    }
    val view = toUserView(user)
    resolveAvatar(view)
    return view
}

internal fun makeProfilePath(template: String) = "src/main/resources/templates/Profiles/$template.html"

suspend fun respondUserTemplate(call: ApplicationCall, template: String) {
    try {
        val userView = getSessionUser(call) ?: return
        call.respond(
            FreeMarkerContent(
                template,
                mapOf("user" to userView)
            )
        )
    } catch (e: Exception) {
        call.respondErrorDialog(e.message!!)
    }
}

suspend fun ApplicationCall.respondSettings() {
    respondUserTemplate(this, makeProfilePath("ProfileSettings"))
}

suspend fun ApplicationCall.respondProfile(user: User) {
    try {
        respond(
            FreeMarkerContent(
                makeProfilePath(
                    if (isEmployee(user.employerOrEmployee)) "EmployeeProfile" else "EmployerProfile"
                ), mapOf("user" to getUserView(this, user))
            )
        )
    } catch (e: Exception) {
        respondErrorDialog(e.message!!)
    }
}

suspend fun ApplicationCall.respondPasswordChangeForm() {
    respondUserTemplate(this, makeProfilePath("PasswordChangeForm"))
}