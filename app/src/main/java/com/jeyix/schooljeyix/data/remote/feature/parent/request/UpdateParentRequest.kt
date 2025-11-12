package com.jeyix.schooljeyix.data.remote.feature.parent.request

data class UpdateParentRequest(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String?
)