package com.weak_project.views

import com.weak_project.models.Job
import com.weak_project.models.JobModel
import com.weak_project.models.UserModel
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import java.io.File

internal data class StartScreenJobView(
    val roleName: String,
    val companyName: String,
    val country: String,
    var avatarPath: String,
    val id: Int,
    val ownerId: Int
)

internal fun toStartScreenJobView(view: Job) = StartScreenJobView(
    roleName = view.roleName,
    companyName = view.companyName,
    country = view.country,
    avatarPath = "",
    id = view.id,
    ownerId = view.ownerId
)

/**
 * Setup profile picture by following rule: if user has avatar in database, it has
 * set as profile picture to render; otherwise default avatar is set.
 */
internal fun resolveAvatar(view: StartScreenJobView) {
    val avatar = UserModel.getAvatar(view.ownerId)
    if (avatar != null) {
        view.avatarPath = "/static/avatar${view.ownerId}.png"
        val realAvatarPath = "build/resources/main/files/avatar${view.ownerId}.png"
        val file = File(realAvatarPath)
        if (!file.exists()) {
            file.writeBytes(avatar)
        } else {
            if (!avatar.contentEquals(file.readBytes())) {
                file.writeBytes(avatar)
            }
        }
    } else {
        view.avatarPath = "/static/NoAvatar.png"
    }
}

internal fun getSomeJobViews(): MutableList<StartScreenJobView> {
    var views = mutableListOf<StartScreenJobView>()

    JobModel.getSome(maxCount = 15).forEach { job ->
        val view = toStartScreenJobView(job)
        resolveAvatar(view)
        views.add(view)
    }

    return views
}

internal fun makeStartScreenPath(template: String) = "src/main/resources/templates/StartScreen/$template.html"

suspend fun ApplicationCall.respondStartScreen() {
    respond(
        FreeMarkerContent(
            makeStartScreenPath("StartScreen"),
            mapOf(
                "jobs" to getSomeJobViews()
            )
        )
    )
}