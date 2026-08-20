package com.example

import io.ktor.server.application.*
import io.ktor.server.sessions.*

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
}