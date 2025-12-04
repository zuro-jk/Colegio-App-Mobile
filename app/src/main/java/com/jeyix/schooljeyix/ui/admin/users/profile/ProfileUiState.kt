package com.jeyix.schooljeyix.ui.admin.users.profile

import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: UserProfileResponse? = null,
    val error: String? = null
)