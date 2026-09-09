package com.example.repositories

import com.example.entities.Session
import com.example.helper.DatabaseHelper.Sessions
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

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
}