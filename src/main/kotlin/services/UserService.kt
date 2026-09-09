package com.example.services

import com.example.models.RegisterUserModel
import com.example.models.UserModel
import com.example.repositories.UserRepository
import com.example.utils.Security
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.slf4j.LoggerFactory


class UserService(val database: Database) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val userRepository = UserRepository(database)

    suspend fun register(user: RegisterUserModel): Long {
        if (userRepository.isEmailAvailable(user.email))
            return userRepository.save(user)
        else
            return -2L
    }

    suspend fun getByEmail(email: String): UserModel? = suspendTransaction(database) {
        return@suspendTransaction userRepository.findByEmail(email)
    }
    suspend fun getById(userId: Long): UserModel? = suspendTransaction(database) {
        return@suspendTransaction userRepository.findById(userId)
    }

    suspend fun checkPassword(userId: Long, password: String): Boolean = suspendTransaction(database){
        val passwordHash = userRepository.getPasswordHash(userId)
        passwordHash?.let {
            Security.checkPasswords(password, it)
        } ?: false
    }
    suspend fun updatePassword(email: String, password: String): Boolean = suspendTransaction(database){
        val user = userRepository.findByEmail(email) ?: return@suspendTransaction false
        userRepository.updatePasswordHash(user.id, password)
    }
    suspend fun updatePassword(userId: Long, password: String): Boolean {
        return userRepository.updatePasswordHash(userId, Security.encryptPassword(password))
    }

    suspend fun editAccount(usersId: Long, newUserModel: UserModel): Int = suspendTransaction(database){
        val email = newUserModel.email
        val name = newUserModel.name

        val user = userRepository.findByEmail(email) ?: return@suspendTransaction -1
        if(user.email != email)
            if(!userRepository.isEmailAvailable(email)) return@suspendTransaction 1
                userRepository.updateEmail(user.id, email)

        if(user.name != name)
            userRepository.updateName(user.id, name)
        return@suspendTransaction 0
    }

    suspend fun read(id: Long): UserModel? {
        return userRepository.findById(id)
    }

    suspend fun delete(id: Long) : Boolean {
        return userRepository.delete(id)
    }
}