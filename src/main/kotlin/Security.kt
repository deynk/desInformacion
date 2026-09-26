package com.example

import com.example.helper.DatabaseHelper
import com.example.models.SessionModel
import com.example.services.SessionService
import com.example.values.Values
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.session
import io.ktor.server.response.respondRedirect
import io.ktor.server.sessions.*

suspend fun Application.configureSecurity() {
    val database = DatabaseHelper().createConnection()
    DatabaseHelper().createSchema(database)
    val sessionService = SessionService(database)

    install(Sessions) {
        cookie<SessionModel>("user_session") {
            cookie.path = "/"   // The route in the client where the cookie is stored
            cookie.secure = true    // The JS client can't access the cookie
            cookie.httpOnly = true  // Only HTTPS connections are allowed
            cookie.sameSite = "lax"
            cookie.maxAgeInSeconds = Values().sessionExpirationTime / 1000L    // The value is in ms so it's converted into seconds.
        }
    }

    install(Authentication) {
        session<SessionModel> {
            validate{ session ->
                if(sessionService.exists(session.token)) session
                else null
            }
            challenge {
                call.respondRedirect("/login.html")
            }
        }
    }
}