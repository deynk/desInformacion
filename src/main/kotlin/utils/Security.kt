package com.example.utils

import org.mindrot.jbcrypt.BCrypt

object Security {
    fun encryptPassword(password: String): String{ return BCrypt.hashpw(password, BCrypt.gensalt(12)) }

    fun checkPasswords(passwordPlain: String?, hashedPassword: String?): Boolean{
        return BCrypt.checkpw(passwordPlain, hashedPassword)
    }
}