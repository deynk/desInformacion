package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class SessionModel(
    val token: String
)