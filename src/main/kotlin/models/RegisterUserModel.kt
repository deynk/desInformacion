package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserModel(
    val name: String,
    val email: String,
    val password: String
)