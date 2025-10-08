package com.jeyix.schooljeyix.data.remote.feature.auth.response

data class AuthResponse (
    val accessToken: String,
    val sessionId: Number,
    val user: UserProfileResponse
)