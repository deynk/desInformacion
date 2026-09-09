package com.example.values

data class Values(
    // DB
    val dbUrl : String = env("DB_URL"),
    val dbUserName : String = env("DB_USERNAME"),
    val dbUserPassword : String = env("DB_PASSWORD"),

    // Session tokens
    val secretTokenKey : String = env("SECRET_TOKEN_KEY"),
    val refreshSecretKey : String = env("REFRESH_SECRET_KEY"),
    val refreshExpirationTimeToken : Long = env("REFRESH_EXPIRATION_TIME").toLong(),
    val accessSecretKey : String = env("ACCESS_SECRET_KEY"),
    val accessExpirationTimeToken : Long = env("ACCESS_EXPIRATION_TIME").toLong(),
    val sessionExpirationTime : Long = env("SESSION_EXPIRATION_TIME").toLong(),
)

fun env(name: String): String =
    System.getenv(name) ?: error("Environment variable '$name' not set")
