package com.weak_project.controllers

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import com.weak_project.models.*
import com.weak_project.views.*
import com.weak_project.sessions.*

class ProfileController {
    /// TODO: Decide where to store username.
    suspend fun setupProfile(call: ApplicationCall) {
        val session = call.sessions.get<UserSession>()
        if (session == null) {
            call.respondErrorDialog("Session does not exist or is expired.")
            return
        }

        val firstName = call.parameters["firstName"]!!
        val lastName = call.parameters["lastName"]!!
        val country = call.parameters["country"]!!
        val city = call.parameters["city"]!!
        val birthDate = call.parameters["birthDate"]!!
        val gender = resolveGenderFromString(call.parameters["gender"]!!)
        val phone = call.parameters["phone"]!!

        try {
            ProfileModel.setupProfile(
                session.username,
                firstName,
                lastName,
                country,
                city,
                birthDate,
                gender,
                phone
            )
        } catch (e: Exception) {
            call.respondErrorDialog(e.message!!)
            return
        }

        call.respondRedirect("/profile")
    }
}

/// By ISO/IEC 5218.
fun resolveGenderFromString(gender: String): Int {
    return when (gender) {
        "Male" -> 1
        "Female" -> 2
        else -> throw RuntimeException("Wrong gender.")
    }
}

/// By ISO/IEC 5218.
fun resolveGenderFromInt(gender: Int): String {
    return when (gender) {
        1 -> "Male"
        2 -> "Female"
        else -> throw RuntimeException("Wrong gender code.")
    }
}

fun Routing.profile(controller: ProfileController) {
    get("/profile") { call.respondEmployeeProfile() }
    get("/setup_profile") { call.respondSettings() }
    get("confirm_setup_profile") { controller.setupProfile(call) }
}