package com.jeyix.schooljeyix.data.remote.feature.users.response

data class UserSessionResponse(
    val sessionId: Long,
    val expiryDate: String,
    val ip: String,
    val userAgent: String
)
