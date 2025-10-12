package com.jeyix.schooljeyix.data.remote.feature.users.response

import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse

data class UpdateProfileResponse(
    val user: UserProfileResponse,
    val token: String
)

