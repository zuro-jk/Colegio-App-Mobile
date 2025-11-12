package com.jeyix.schooljeyix.data.remote.feature.student.response

data class UserSummary(
    val id: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val email: String,
    val phone: String?,
    val profileImageUrl: String?
)