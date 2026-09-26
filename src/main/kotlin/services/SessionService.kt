package com.example.services

import com.example.entities.Session
import com.example.models.UserModel
import com.example.repositories.SessionRepository
import com.example.utils.Auth
import com.example.utils.Security
import org.jetbrains.exposed.v1.jdbc.Database
import org.slf4j.LoggerFactory

class SessionService(val database: Database) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val sessionRepository = SessionRepository(database)

    suspend fun createSession(user: UserModel): Session {
        val newSession = Session(
            user.id,
            Security.encryptPassword(
                Auth().generateSessionToken()
            ),
        )
        val tokenHash = sessionRepository.save(newSession)

        return newSession
    }

    suspend fun getByToken(token: String): Session? {
        return sessionRepository.getByTokenHash(token)
    }
    suspend fun getByUserId(userId: Long): Session? {
        return sessionRepository.getByUserId(userId)
    }

    /** Erases the expired sessions and returns a valid session */
    suspend fun checkByUserId(userId: Long): Session? {
        sessionRepository.expireByDateByUserId(userId)
        return sessionRepository.getByUserId(userId)
    }

    suspend fun exists(token: String): Boolean = getByToken(token) != null
}