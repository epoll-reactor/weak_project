package com.weak_project.mvc.profile

import com.weak_project.view.respondDialog
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

class ProfileController {
    /// TODO: Decide where to store username.
    suspend fun setupProfile(call: ApplicationCall) {
        val username = "a" ///< Just for debug.
        val firstName = call.parameters["firstName"]!!
        val lastName = call.parameters["lastName"]!!
        val country = call.parameters["country"]!!
        val city = call.parameters["city"]!!
        val birthDate = call.parameters["birthDate"]!!
        val gender = resolveGenderFromString(call.parameters["gender"]!!)
        val phone = call.parameters["phone"]!!

        try {
            ProfileModel.setupProfile(
                username,
                firstName,
                lastName,
                country,
                city,
                birthDate,
                gender,
                phone
            )
        } catch (e: Exception) {
            call.respondDialog(e.message!!)
            return
        }

        call.respondRedirect("/profile")
    }
}

/// By ISO/IEC 5218.
fun resolveGenderFromString(gender: String): Int {
    return if (gender == "Male") 1 else 2
}

/// By ISO/IEC 5218.
fun resolveGenderFromInt(gender: Int): String {
    return if (gender == 1) "Male" else "Female"
}

fun Routing.profile(controller: ProfileController) {
    get("/profile") { call.respondProfile() }
    get("/setup_profile") { call.respondSettings() }
    get("confirm_setup_profile") { controller.setupProfile(call) }
}