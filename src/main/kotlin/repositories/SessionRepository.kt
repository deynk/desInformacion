package com.example.repositories

import com.example.entities.Session
import com.example.helper.DatabaseHelper.Sessions
import com.example.helper.DatabaseHelper.Users
import com.example.models.UserModel
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import kotlin.time.Clock
import kotlin.time.Instant

class SessionRepository(val database: Database) {
    suspend fun save(session: Session): String = suspendTransaction(database) {
        addLogger(StdOutSqlLogger)

        val newRecord = Sessions.insert {
            it[userId] = session.userId
            it[tokenHash] = session.tokenHash
            it[deviceName] = session.deviceName
            it[creationDate] = session.creationDate
            it[lastLoginDate] = session.lastLoginDate
            it[expirationDate] = session.expirationDate
        }
        newRecord[Sessions.tokenHash]
    }

    suspend fun getByTokenHash(tokenHash: String): Session? = suspendTransaction(database) {
        Sessions.selectAll().where{ Sessions.tokenHash eq tokenHash }.singleOrNull()?.toSession()
    }
    suspend fun getByUserId(userId: Long): Session? = suspendTransaction(database) {
        Sessions.selectAll().where { Sessions.userId eq userId }.singleOrNull()?.toSession()
    }

    /** Deletes all sessions from a user */
    suspend fun deleteByUserId(userId: Long) = suspendTransaction(database) {
        Sessions.deleteWhere { Sessions.userId eq userId }
    }

    /** Deletes a single session by its token **/
    suspend fun deleteByToken(token: String) = suspendTransaction(database) {
        Sessions.deleteWhere { Sessions.tokenHash eq token }
    }

    fun ResultRow.toSession(): Session {
        return Session(
            this[Sessions.userId],
            this[Sessions.tokenHash],
            this[Sessions.deviceName],
            this[Sessions.creationDate],
            this[Sessions.lastLoginDate],
            this[Sessions.expirationDate],
        )
    }
}