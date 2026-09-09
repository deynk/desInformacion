package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.values.Values
import java.util.Date
import java.util.UUID

class Auth {
    fun generateAccesToken(userId: Long): String {
        return JWT.create()
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + Values().accessExpirationTimeToken))
            .sign(Algorithm.HMAC256(Values().accessSecretKey))
    }
    fun generateRefreshToken(userId: Long): String {
        return JWT.create()
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + Values().refreshExpirationTimeToken))
            .sign(Algorithm.HMAC256(Values().refreshSecretKey))
    }

    fun generateSessionToken(): String {
        return UUID.randomUUID().toString()
    }
}