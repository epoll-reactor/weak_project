package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import com.weak_project.models.CV

suspend fun ApplicationCall.respondCVFindDialog() {
    respondTemplate("src/main/resources/templates/CVs/FindCVsDialog.html")
}

suspend fun ApplicationCall.respondCVList(CVs: MutableList<CV>) {
    respond(
        FreeMarkerContent(
            "src/main/resources/templates/CVs/CVList.html",
            mapOf("CVs" to CVs)
        )
    )
}