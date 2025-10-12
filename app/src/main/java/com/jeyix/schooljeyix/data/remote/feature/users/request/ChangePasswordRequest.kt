package com.jeyix.schooljeyix.data.remote.feature.users.request

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)

