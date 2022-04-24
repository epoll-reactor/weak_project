package com.weak_project.controllers

import com.weak_project.views.respondCVFindDialog
import com.weak_project.views.respondErrorDialog
import io.ktor.application.*
import io.ktor.routing.*


class EmployerController {
    suspend fun findCVs(call: ApplicationCall) {
        val keySkillsList = call.parameters["keySkills"]!!.split(",")
        val spokenLanguagesList = call.parameters["spokenLanguages"]!!.split(",")
        val country = call.parameters["country"]!!
        val education = call.parameters["education"]!!
    }
}

fun Routing.employer(controller: EmployerController) {
    get("/find_cvs") { call.respondCVFindDialog() }
    get("/commit_cvs_search") { controller.findCVs(call) }
}