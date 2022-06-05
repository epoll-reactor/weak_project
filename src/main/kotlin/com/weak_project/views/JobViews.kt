package com.weak_project.views

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import com.weak_project.models.Job
import com.weak_project.models.User
import io.ktor.sessions.*

suspend fun ApplicationCall.respondJobList(jobs: MutableList<Job>) {
    if (jobs.isEmpty()) {
        respondErrorDialog("Sorry, we cannot satisfy your criteria")
        return
    }

    val session = sessions.get<User>()
    if (session == null) {
        respondLogin()
        return
    }

    respond(
        FreeMarkerContent(
            makeJobsPath("JobsList"),
            mapOf(
                "sessionId" to session.id,
                "jobs" to jobs
            )
        )
    )
}