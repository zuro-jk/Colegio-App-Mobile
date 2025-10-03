package com.jeyix.schooljeyix.data.datasource

data class LoginRequest(
    val usernameOrEmail: String,
    val password: String
)