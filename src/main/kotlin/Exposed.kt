package com.example

import com.example.helper.DatabaseHelper
import com.example.helper.DatabaseHelper.Users.id
import com.example.models.LoginModel
import com.example.models.RegisterUserModel
import com.example.models.UserModel
import com.example.services.SessionService
import com.example.services.UserService
import io.ktor.http.Cookie
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.request.requireCookie
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import java.lang.Thread.sleep

suspend fun Application.configureExposed() {
    val database = DatabaseHelper().createConnection()
    DatabaseHelper().createSchema(database)

    val userService = UserService(database)
    val sessionService = SessionService(database)

    routing {
        //TODO: Eliminar esto en producción
        /** Elimina todas las tablas al buscar "localhost:8080/resetDB" */
        get("/resetDB"){
            DatabaseHelper().dropSchema(database)
            DatabaseHelper().createSchema(database)

            println("DB cleared")
            call.respondText("DB cleared", null, HttpStatusCode.OK)
        }


        // Create user
        post("/users") {
            val user = call.receive<RegisterUserModel>()

            val id = userService.register(user)
            when (id) {
                -1L -> call.respond(HttpStatusCode.BadRequest)
                -2L -> call.respond(HttpStatusCode.Conflict)
                else -> {
                    call.respond(HttpStatusCode.Created)
                }
            }
        }
        post("/users/login") {
            val startTime = System.currentTimeMillis()

            val loginModel = call.receive<LoginModel>()
            val foundUser = userService.getByEmail(loginModel.email)

            var authenticated = false
            if (foundUser != null) authenticated = userService.checkPassword(foundUser.id, loginModel.password)

            if(authenticated) sessionService.createSession(foundUser!!)

            // Delay para responder de manera uniforme
            val elapsed = System.currentTimeMillis() - startTime
            if(elapsed < 200) {
                sleep(200 - elapsed)
            }

            if (authenticated) {
                call.respond(
                    HttpStatusCode.OK/*,
                    mapOf(
                        "id" to foundUser?.id,
                        "name" to foundUser?.name,
                        "email" to foundUser?.email
                    )*/
                )
            } else call.respond(HttpStatusCode.Unauthorized)
        }

        /*
        // Read user
        get("/users/{id}") {
            val id = call.parameters["id"]?.toUInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // Update user
        put("/users/{id}") {
            val id = call.parameters["id"]?.toUInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<ExposedUser>()
            userService.update(id, user)
            call.respond(HttpStatusCode.NoContent)
        }

        // Delete user
        delete("/users/{id}") {
            val id = call.parameters["id"]?.toUInt() ?: throw IllegalArgumentException("Invalid ID")
            userService.delete(id)
            call.respond(HttpStatusCode.NoContent)
        }
        */
    }
}
