package com.weak_project.controllers

import com.weak_project.views.respondStartScreen
import io.ktor.application.*
import io.ktor.routing.*

fun Routing.startScreen() {
    get("/") { call.respondStartScreen() }
}