package com.jeyix.schooljeyix.data.remote.feature.parent.response

data class UserResponseSummary(
    val id: Long,
    val username: String,
    val email: String,
    val fullName: String,
    val phone: String,
    val profileImageUrl: String
)
