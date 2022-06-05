package com.weak_project.controllers

import com.weak_project.models.*
import com.weak_project.views.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import respondAddCVDialog
import respondCVById
import respondEditCVDialog

class CVController {
    suspend fun commitAddCV(call: ApplicationCall) {
        val keySkills = call.parameters["keySkills"]!!
        val spokenLanguages = call.parameters["spokenLanguages"]!!
        val education = call.parameters["education"]!!

        val session = call.sessions.get<User>()
        if (session == null) {
            call.respondLogin()
            return
        }

        CVModel.insert(
            skills = keySkills,
            languages = spokenLanguages,
            theCountry = "Placeholder",
            theEducation = education,
            ownerId__ = session.id
        )

        call.respondRedirect("/profile/id${session.id}")
    }

    suspend fun commitEditCV(call: ApplicationCall) {
        val keySkills = call.parameters["keySkills"]!!
        val spokenLanguages = call.parameters["spokenLanguages"]!!
        val education = call.parameters["education"]!!

        val session = call.sessions.get<User>()
        if (session == null) {
            call.respondLogin()
            return
        }

        CVModel.update(
            skills_ = keySkills,
            languages_ = spokenLanguages,
            theCountry_ = "Placeholder",
            theEducation_ = education,
            ownerId_ = session.id
        )

        call.respondRedirect("/cv/id${session.id}")
    }
}

fun Routing.cvs(controller: CVController) {
    get("/cv/id{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        call.respondCVById(call, id)
    }
    get("/edit_cv/id{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        call.respondEditCVDialog(CVModel.get(id)!!)
    }
    get("/commit_edit_cv") { controller.commitEditCV(call) }
    get("/add_cv") { call.respondAddCVDialog() }
    get("/commit_add_cv") { controller.commitAddCV(call) }
}

