package com.jeyix.schooljeyix.ui.admin.users.adminTab

import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse

data class AdminStaffListUiState(
    val isLoading: Boolean = true,
    val staff: List<UserProfileResponse> = emptyList(),
    val error: String? = null
)