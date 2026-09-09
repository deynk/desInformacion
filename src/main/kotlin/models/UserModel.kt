package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class UserModel (
    val id: Long,
    val name: String,
    val email: String
)