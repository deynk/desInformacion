package com.example.services

import com.example.entities.Session
import com.example.models.RegisterUserModel
import com.example.models.UserModel
import com.example.repositories.SessionRepository
import com.example.utils.Auth
import com.example.utils.Security
import org.jetbrains.exposed.v1.jdbc.Database
import org.slf4j.LoggerFactory

class SessionService(val database: Database) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val sessionRepository = SessionRepository(database)

    suspend fun createSession(user: UserModel): String {
        val newSession = Session(
            user.id,
            Security.encryptPassword(
                Auth().generateSessionToken()
            ),
        )
        val tokenHash = sessionRepository.save(newSession)

        return newSession.tokenHash
    }
}