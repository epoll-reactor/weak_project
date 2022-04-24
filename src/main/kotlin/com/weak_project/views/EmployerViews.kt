package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*

suspend fun ApplicationCall.respondCVFindDialog() {
    respondTemplate("src/main/resources/templates/CVs/FindCVsDialog.html")
}