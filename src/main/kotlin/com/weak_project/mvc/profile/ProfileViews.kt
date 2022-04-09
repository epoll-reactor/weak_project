package com.weak_project.mvc.profile

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import com.weak_project.mvc.user.User
import com.weak_project.view.respondDialog

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

suspend fun ApplicationCall.respondSettings() {
    respondTemplate("src/main/kotlin/com/weak_project/mvc/profile/ProfileSettings.html")
}

suspend fun ApplicationCall.respondProfile() {
    val username = "a" /// Just for debug
    val user = ProfileModel.getByUsername(username)
    if (user == null) {
        respondDialog("Username $username not found")
        return
    }
    val userView = toUserView(user)
    respond(
        FreeMarkerContent(
            "src/main/kotlin/com/weak_project/mvc/profile/Profile.html",
            mapOf("user" to userView)
        )
    )
}