package com.weak_project.mvc.profile

import com.weak_project.view.respondDialog
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.routing.*

class ProfileController {
    /// TODO: Decide where to store username.
    suspend fun setupProfile(call: ApplicationCall) {
        println(call.parameters)

        val firstName = call.parameters["firstName"]!!
        val lastName = call.parameters["lastName"]!!
        val country = call.parameters["country"]!!
        val city = call.parameters["city"]!!
        val birthDate = call.parameters["birthDate"]!!
        val gender = resolveGenderByISO5218(call.parameters["gender"]!!)
        val phone = call.parameters["phone"]!!

        try {
            ProfileModel.setupProfile(
                "a", ///< Just for debug.
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
        }

        call.respondTemplate("src/main/kotlin/com/weak_project/mvc/profile/Profile.html")
    }

    suspend fun respondSettings(call: ApplicationCall) {
        call.respondTemplate("src/main/kotlin/com/weak_project/mvc/profile/ProfileSettings.html")
    }

    private fun resolveGenderByISO5218(gender: String): Int {
        return if (gender == "Male") 1 else 2
    }
}

fun Routing.installProfileRoutes(controller: ProfileController) {
    get("/setup_profile") { controller.respondSettings(call) }
    get("confirm_setup_profile") { controller.setupProfile(call) }
}