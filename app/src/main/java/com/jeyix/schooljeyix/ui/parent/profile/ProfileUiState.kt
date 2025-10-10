package com.jeyix.schooljeyix.ui.parent.profile

import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse

data class ProfileUiState(
    val userProfile: UserProfileResponse? = null,
    val isLoggedOut: Boolean = false
)
