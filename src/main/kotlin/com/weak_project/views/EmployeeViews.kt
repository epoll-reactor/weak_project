import com.weak_project.models.CV
import com.weak_project.models.CVModel
import com.weak_project.views.getSessionUser
import com.weak_project.views.respondErrorDialog
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*

internal fun makeCVsPath(template: String) = "src/main/resources/templates/CVs/$template.html"

suspend fun ApplicationCall.respondCVById(call: ApplicationCall, id: Int) {
    val cv: CV? = CVModel.get(id)
    val userView = getSessionUser(call) ?: return
    if (cv == null) {
        respondErrorDialog("CV with id $id not found.")
    }

    respond(
        FreeMarkerContent(
            makeCVsPath("CV"),
            mapOf(
                "cv" to cv,
                "user" to userView
            )
        )
    )
}

suspend fun ApplicationCall.respondAddCVDialog() {
    val cv = CV(
        id = 0,
        keySkills = "",
        spokenLanguages = "",
        country = "",
        education = "",
        ownerId = 0
    )
    respond(
        FreeMarkerContent(
            makeCVsPath("CVDialog"),
            mapOf(
                "cv" to cv,
                "actionPath" to "/commit_add_cv"
            )
        )
    )
}