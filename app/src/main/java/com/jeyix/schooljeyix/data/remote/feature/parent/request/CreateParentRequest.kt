package com.jeyix.schooljeyix.data.remote.feature.parent.request

data class CreateParentRequest(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String?
)
