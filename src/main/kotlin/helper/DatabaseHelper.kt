package com.example.helper

import com.example.values.Values
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

class DatabaseHelper {
    // Tables
    object Users : Table() {
        val id = long("id").autoIncrement()
        val name = varchar("name", 50)
        val email = varchar("email", 320).uniqueIndex()
        val passwordHash = varchar("password_hash", 255)
        val createdAt = long("created_at").default(System.currentTimeMillis())
        val verified = bool("verified").default(false)

        override val primaryKey = PrimaryKey(id)
    }

    object Sessions : Table() {
        val id = long("id").autoIncrement()
        val userId = reference("user_id", Users.id, ReferenceOption.CASCADE)
        val tokenHash = varchar("token_hash", 255).uniqueIndex()
        val deviceName = varchar("device_name", 255)
        val creationDate = long("creation_date").default(System.currentTimeMillis())
        val lastLoginDate = long("last_login_date").nullable()
        val expirationDate = long("expiration_date").default(System.currentTimeMillis() + Values().sessionExpirationTime).index("expiration_date")
        override val primaryKey = PrimaryKey(id)
    }

    object News : Table() {
        val id = long("id").autoIncrement()
        val uuid = varchar("uuid", 255).uniqueIndex()

        val title = varchar("title", 200)
        val description = varchar("description", 500)
        val body = mediumText("body")

        val fakeDate = long("fake_date")
        val createdAt = long("created_at").default(System.currentTimeMillis())

        val fakeAuthor = varchar("fake_author", 255)
        val userId = long("userId")
        override val primaryKey = PrimaryKey(id)
    }

    fun createConnection() : Database {
        return Database.connect(
            url = Values().dbUrl,
            driver = "org.mariadb.jdbc.Driver",
            user = Values().dbUserName,
            password = Values().dbUserPassword
        )
    }

    suspend fun createSchema(database: Database) {
        suspendTransaction(database) {
            SchemaUtils.create(
                Users,
                Sessions,
            )
        }
    }

    suspend fun dropSchema(database: Database) {
        suspendTransaction(database) {
            SchemaUtils.drop(
                Users,
                Sessions,
            )
        }
    }
}