package com.example.repositories

import com.example.helper.DatabaseHelper.*
import com.example.models.RegisterUserModel
import com.example.models.UserModel
import com.example.utils.Security
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update


class UserRepository(val database: Database) {
    suspend fun save(user: RegisterUserModel): Long = suspendTransaction(database) {
        val newRecord = Users.insert {
            it[name] = user.name
            it[email] = user.email
            it[passwordHash] = Security.encryptPassword(user.password)
        }
        newRecord[Users.id]
    }

    suspend fun findByEmail(email: String): UserModel? = suspendTransaction(database) {
        Users.selectAll().where { Users.email eq email }.singleOrNull()?.toUserModel()
    }
    suspend fun findById(userId: Long): UserModel? = suspendTransaction(database) {
        Users.selectAll().where { Users.id eq userId}.singleOrNull()?.toUserModel()
    }

    fun getPasswordHash(userId: Long): String? {
        return Users.select(Users.passwordHash).where{ Users.id eq userId }.map { it[Users.passwordHash] }.singleOrNull()
    }

    /** Returns if an email is available. True: the email is available. False: the email is not available. */
    suspend fun isEmailAvailable(email: String): Boolean = suspendTransaction(database) {
        Users.selectAll().where(Users.email eq email).none()
    }

    fun ResultRow.toUserModel(): UserModel {
        return UserModel(
            id = this[Users.id],
            name = this[Users.name],
            email = this[Users.email]
        )
    }

    suspend fun read(userId: Long): UserModel? {
        return suspendTransaction(database) {
            Users.selectAll()
                .where { Users.id eq userId }
                .map {
                    UserModel(
                    it[Users.id],
                    it[Users.name],
                    it[Users.email]
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun updateName(userId: Long, newName: String) : Boolean = suspendTransaction(database) {
        val updatedRows =
            Users.update({ Users.id eq userId }) {
                it[name] = newName
            }
        updatedRows > 0 // true si se actualizó al menos un registro
    }
    suspend fun updateEmail(userId: Long, newEmail: String) : Boolean = suspendTransaction(database) {
        val updatedRows =
            Users.update({ Users.id eq userId }) {
                it[email] = newEmail
            }
        updatedRows > 0 // true si se actualizó al menos un registro
    }
    suspend fun updatePasswordHash(userId: Long, newPasswordHash: String) : Boolean = suspendTransaction(database) {
        val updatedRows =
            Users.update({ Users.id eq userId }) {
                it[passwordHash] = newPasswordHash
            }
        updatedRows > 0 // true si se actualizó al menos un registro
    }

    suspend fun delete(userId: Long) : Boolean = suspendTransaction(database) {
         val deletedRows = Users.deleteWhere { Users.id.eq(userId) }
        deletedRows > 0 // true si se eliminó al menos un registro
    }
}