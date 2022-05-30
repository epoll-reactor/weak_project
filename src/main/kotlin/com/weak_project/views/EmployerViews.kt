package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import com.weak_project.models.*
import java.io.File

internal fun makeCVsPath(template: String) = "src/main/resources/templates/CVs/$template.html"
internal fun makeJobsPath(template: String) = "src/main/resources/templates/Jobs/$template.html"

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

suspend fun ApplicationCall.respondEditJobDialog(job: Job) {
    respond(
        FreeMarkerContent(
            makeJobsPath("JobDialog"),
            mapOf(
                "job" to job,
                "windowTitle" to "Edit job",
                "actionPath" to "/commit_edit_job"
            )
        )
    )
}

suspend fun ApplicationCall.respondFindJobDialog() {
    val job = Job(
        id = 0,
        roleName = "",
        description = "",
        companyName = "",
        country = "",
        keySkills = "",
        spokenLanguages = "",
        requiredEducation = "",
        ownerId = 0
    )
    respond(
        FreeMarkerContent(
            makeJobsPath("JobDialog"),
            mapOf(
                "job" to job,
                "windowTitle" to "Find job",
                "actionPath" to "/commit_find_job"
            )
        )
    )
}

suspend fun ApplicationCall.respondAddJobDialog() {
    val job = Job(
        id = 0,
        roleName = "",
        description = "",
        companyName = "",
        country = "",
        keySkills = "",
        spokenLanguages = "",
        requiredEducation = "",
        ownerId = 0
    )
    respond(
        FreeMarkerContent(
            makeJobsPath("JobDialog"),
            mapOf(
                "job" to job,
                "windowTitle" to "Add job",
                "actionPath" to "/commit_add_job"
            )
        )
    )
}

suspend fun ApplicationCall.respondJobById(id: Int) {
    val job: Job? = JobModel.get(id)
    if (job == null) {
        respondErrorDialog("Job with id $id not found.")
        return
    }

    val avatar = UserModel.getAvatar(job.ownerId)
    var avatarPath = ""

    if (avatar != null) {
        avatarPath = "/static/avatar${job.ownerId}.png"
        val realAvatarPath = "build/resources/main/files/avatar${job.ownerId}.png"
        val file = File(realAvatarPath)
        if (!file.exists()) {
            file.writeBytes(avatar)
        }
    } else {
        avatarPath = "/static/NoAvatar.png"
    }

    respond(
        FreeMarkerContent(
            makeJobsPath("Job"),
            mapOf(
                "job" to job,
                "employerAvatar" to avatarPath
            )
        )
    )
}