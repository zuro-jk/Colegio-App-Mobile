package com.jeyix.schooljeyix.data.remote.feature.auth.request

data class LoginRequest(
    val usernameOrEmail: String,
    val password: String
)