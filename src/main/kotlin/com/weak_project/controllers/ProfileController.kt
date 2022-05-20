package com.weak_project.controllers

import com.weak_project.IO.uploadFile
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import com.weak_project.models.*
import com.weak_project.views.*
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import java.io.File
import io.ktor.client.request.forms.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.response.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import kotlinx.html.InputType

/// By ISO/IEC 5218.
internal fun resolveGenderFromString(gender: String) =
    when (gender) {
        "Male" -> 1
        "Female" -> 2
        else -> throw RuntimeException("Wrong gender.")
    }

/// By ISO/IEC 5218.
internal fun resolveGenderFromInt(gender: Int) =
    when (gender) {
        1 -> "Male"
        2 -> "Female"
        else -> throw RuntimeException("Wrong gender code.")
    }

class ProfileController {
    private fun validateBirthDate(date: String) =
        """^\d{1,2}/\d{1,2}/[1,2]\d{3}$""".toRegex().containsMatchIn(date)

    private fun validatePhoneNumber(phoneNumber: String) =
        // Super exciting and pretty.
        """^(\+\d{2,3})?((\d{9})|(\d{3}[\-]){2}\d{3})$""".toRegex().containsMatchIn(phoneNumber)

    /**
     * Update profile information.
     *
     * NOTE: all fields are required by HTML form.
     */
    suspend fun setupProfile(call: ApplicationCall) {
        val session = call.sessions.get<User>()
        if (session == null) {
            call.respondLogin()
            return
        }

        val firstName = call.parameters["firstName"]!!
        val lastName = call.parameters["lastName"]!!
        val country = call.parameters["country"]!!
        val city = call.parameters["city"]!!
        val birthDate = call.parameters["birthDate"]!!
        val gender = resolveGenderFromString(call.parameters["gender"]!!)
        val phone = call.parameters["phone"]!!

        if (!validateBirthDate(birthDate)) {
            call.respondErrorDialog("Invalid birth date: $birthDate")
            return
        }

        if (!validatePhoneNumber(phone)) {
            call.respondErrorDialog("Invalid phone number: $phone")
            return
        }

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

        call.respondRedirect("/profile/id${session.id}")
    }

    suspend fun changePassword(call: ApplicationCall) {
        val session = call.sessions.get<User>()
        if (session == null) {
            call.respondLogin()
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

    suspend fun uploadAvatar(call: ApplicationCall) {
        val session = call.sessions.get<User>()
        if (session == null) {
            call.respondLogin()
            return
        }

        val uploadedPath = uploadFile(call, "avatarToUpload${session.id}")

        UserModel.updateAvatar(session.username, uploadedPath)

        File(uploadedPath).delete()

        call.respondRedirect("/profile/id${session.id}")
    }
}

fun Routing.profile(controller: ProfileController) {
    get("/profile/id{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        call.respondProfileById(id)
    }
    get("/setup_profile") { call.respondSettings() }
    get("/setup_password") { call.respondPasswordChangeForm() }
    get("/confirm_setup_profile") { controller.setupProfile(call) }
    get("/confirm_change_password") { controller.changePassword(call) }
    post("/uploadAvatar") { controller.uploadAvatar(call) }
}