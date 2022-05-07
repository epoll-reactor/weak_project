package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import com.weak_project.models.CV

internal fun makeCVsPath(template: String) = "src/main/resources/templates/CVs/$template.html"

suspend fun ApplicationCall.respondCVFindDialog() {
    respondTemplate(makeCVsPath("FindCVsDialog"))
}

suspend fun ApplicationCall.respondCVList(CVs: MutableList<CV>) {
    respond(
        FreeMarkerContent(
            makeCVsPath("CVList"),
            mapOf("CVs" to CVs)
        )
    )
}