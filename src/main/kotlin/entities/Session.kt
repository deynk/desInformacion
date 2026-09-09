package com.example.entities

import com.example.values.Values

data class Session (
    val userId: Long,
    val tokenHash: String,
    val deviceName: String = "",
    val creationDate: Long = System.currentTimeMillis(),
    val lastLoginDate: Long? = null,
    val expirationDate: Long = System.currentTimeMillis() + Values().sessionExpirationTime,
)