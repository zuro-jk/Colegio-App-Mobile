package com.jeyix.schooljeyix.data.remote.feature.users.request

data class CreateAdminRequest(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String?,
    val enabled: Boolean,
    val roles: Set<String>
)