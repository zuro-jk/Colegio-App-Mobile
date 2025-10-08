package com.jeyix.schooljeyix.data.remote.feature.auth.request

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String
)