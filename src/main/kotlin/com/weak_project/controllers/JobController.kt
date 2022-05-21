package com.weak_project.controllers

import com.weak_project.models.JobModel
import com.weak_project.models.User
import com.weak_project.views.respondJobsList
import com.weak_project.views.respondLogin
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

class JobController {
    suspend fun commitAddJob(call: ApplicationCall) {
        val roleName = call.parameters["roleName"]!!
        val description = call.parameters["description"]!!
        val preferredSkills = call.parameters["preferredSkills"]!!
        val preferredLanguages = call.parameters["preferredLanguages"]!!
        val education = call.parameters["education"]!!

        JobModel.insert(
            roleName_ = roleName,
            description_ = description,
            companyName_ = "Placeholder", // \TODO: This should come from employer profile.
            country_ = "Placeholder", // \TODO: This should come from employer profile.
            keySkills_ = preferredSkills,
            spokenLanguages_ = preferredLanguages,
            requiredEducation_ = education
        )

        val session = call.sessions.get<User>()
        if (session == null) {
            call.respondLogin()
            return
        }

        call.respondRedirect("/profile/id${session.id}")
    }
}

fun Routing.jobs(controller: JobController) {
    get("/add_job") { call.respondJobsList() }
    get("/commit_add_job") { controller.commitAddJob(call) }
}