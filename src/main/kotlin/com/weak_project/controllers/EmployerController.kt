package com.weak_project.controllers

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import com.weak_project.models.CV
import com.weak_project.models.CVModel
import com.weak_project.views.*

class EmployerController {
    private lateinit var cvsList: MutableList<CV>

    suspend fun findCVs(call: ApplicationCall) {
        val keySkillsList = call.parameters["keySkills"] ?: ""
        val spokenLanguagesList = call.parameters["spokenLanguages"] ?: ""
        val country = call.parameters["country"] ?: ""
        val education = call.parameters["education"] ?: ""

        cvsList = CVModel.getBy(
            skills = keySkillsList,
            languages = spokenLanguagesList,
            theCountry = country,
            theEducation = education
        )

        call.respondRedirect("/cvs")
    }

    suspend fun respondCVsList(call: ApplicationCall) {
        if (cvsList.isEmpty()) {
            call.respondErrorDialog("No one CV was founded")
        } else {
            call.respondCVList(cvsList)
        }
    }
}

fun Routing.employer(controller: EmployerController) {
    get("/find_cvs") { call.respondCVFindDialog() }
    get("/commit_cvs_search") { controller.findCVs(call) }
    get("/cvs") { controller.respondCVsList(call) }
}