package com.jeyix.schooljeyix.data.remote.feature.parent.response.detail

data class UserDetailSummary(
    val id: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val profileImageUrl: String?,
    val enabled: Boolean,
    val emailVerified: Boolean
)

