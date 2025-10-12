package com.jeyix.schooljeyix.data.remote.feature.users.request

data class UpdateProfileRequest(
  val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val phone: String
)

