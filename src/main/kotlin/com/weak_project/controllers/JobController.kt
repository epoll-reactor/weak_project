package com.weak_project.controllers

import com.weak_project.models.*
import com.weak_project.views.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*

class JobController {
    suspend fun commitAddJob(call: ApplicationCall) {
        val roleName = call.parameters["roleName"]!!
        val description = call.parameters["description"]!!
        val preferredSkills = call.parameters["preferredSkills"]!!
        val preferredLanguages = call.parameters["preferredLanguages"]!!
        val education = call.parameters["education"]!!

        val session = call.sessions.get<User>()
        if (session == null) {
            call.respondLogin()
            return
        }

        JobModel.insert(
            roleName_ = roleName,
            description_ = description,
            companyName_ = "Placeholder", // \TODO: This should come from employer profile.
            country_ = "Placeholder", // \TODO: This should come from employer profile.
            keySkills_ = preferredSkills,
            spokenLanguages_ = preferredLanguages,
            requiredEducation_ = education,
            ownerId_ = session.id
        )

        call.respondRedirect("/profile/id${session.id}")
    }
}

fun Routing.jobs(controller: JobController) {
    get("/job/id{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        call.respondJobById(id)
    }
    get("/edit_job/id{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        call.respondEditJobDialog(JobModel.get(id)!!)
    }
    get("/add_job") { call.respondAddJobDialog() }
    get("/commit_add_job") { controller.commitAddJob(call) }
}