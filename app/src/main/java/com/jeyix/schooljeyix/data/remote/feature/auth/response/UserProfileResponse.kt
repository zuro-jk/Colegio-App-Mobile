package com.jeyix.schooljeyix.data.remote.feature.auth.response

data class UserProfileResponse(
    val id: Long?,
    val username: String?,
    val email: String?,
    val enabled: Boolean?,
    val roles: List<String>?,
    val firstName: String?,
    val lastName: String?,
    val fullName: String?,
    val phone: String?,
    val provider: String?,
    val hasPassword: Boolean?,
    val profileImageUrl: String?,
    val usernameNextChange: String?,
    val emailNextChange: String?
)