package com.weak_project.controllers

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import com.weak_project.models.*
import com.weak_project.views.*

class ProfileController {
    suspend fun setupProfile(call: ApplicationCall) {
        val session = call.sessions.get<User>()
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

        call.respondRedirect("/profile/id${session.username}")
    }

    suspend fun respondProfileById(call: ApplicationCall, id: Int) {
        val user = UserModel.getById(id)
        call.respondProfile(user)
    }

    suspend fun changePassword(call: ApplicationCall) {
        val session = call.sessions.get<User>()
        if (session == null) {
            call.respondErrorDialog("Session does not exist or is expired.")
            return
        }

        val oldPassword = call.parameters["oldPassword"]!!
        val newPassword = call.parameters["newPassword"]!!
        val retypedNewPassword = call.parameters["retypedNewPassword"]!!

        if (ProfileModel.getByUsername(session.username)!!.password != UserModel.hashPassword(oldPassword)) {
            call.respondErrorDialog("Wrong old password.")
            return
        }

        if (newPassword != retypedNewPassword) {
            call.respondErrorDialog("Passwords doesn't match.")
            return
        }

        ProfileModel.changePassword(session.username, UserModel.hashPassword(newPassword))

        call.respondRedirect("/profile/id${session.username}")
    }
}

/// By ISO/IEC 5218.
fun resolveGenderFromString(gender: String) =
    when (gender) {
        "Male" -> 1
        "Female" -> 2
        else -> throw RuntimeException("Wrong gender.")
    }

/// By ISO/IEC 5218.
fun resolveGenderFromInt(gender: Int) =
    when (gender) {
        1 -> "Male"
        2 -> "Female"
        else -> throw RuntimeException("Wrong gender code.")
    }

fun Routing.profile(controller: ProfileController) {
    get("/profile/id{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        controller.respondProfileById(call, id)
    }
    get("/setup_profile") { call.respondSettings() }
    get("/setup_password") { call.respondPasswordChangeForm() }
    get("/confirm_setup_profile") { controller.setupProfile(call) }
    get("/confirm_change_password") { controller.changePassword(call) }
}