package com.weak_project.controllers

import com.weak_project.models.CV
import com.weak_project.models.CVModel
import com.weak_project.views.respondCVFindDialog
import com.weak_project.views.respondCVList
import com.weak_project.views.respondErrorDialog
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*


class EmployerController {
    suspend fun findCVs(call: ApplicationCall) {
        val keySkillsList = call.parameters["keySkills"] ?: ""
        val spokenLanguagesList = call.parameters["spokenLanguages"] ?: ""
        val country = call.parameters["country"] ?: ""
        val education = call.parameters["education"] ?: ""

        val cvsList = CVModel.getBy(
            skills = keySkillsList,
            languages = spokenLanguagesList,
            theCountry = country,
            theEducation = education
        )

        if (cvsList.isEmpty()) {
            call.respondErrorDialog("No one CV was founded")
        } else {
            call.respondCVList(cvsList)
        }

//        val cv: CV? = CVModel.getBy(
//            skills = keySkillsList,
//            languages = spokenLanguagesList,
//            theCountry = country,
//            theEducation = education
//        )
//
//        if (cv != null) {
//            call.respondCVList(mutableListOf(cv))
//        } else {
//            call.respondErrorDialog("CV not found")
//        }
    }
}

fun Routing.employer(controller: EmployerController) {
    get("/find_cvs") { call.respondCVFindDialog() }
    get("/commit_cvs_search") { controller.findCVs(call) }
}