package com.jeyix.schooljeyix.ui.admin.users.staff.form

import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse

data class AdminStaffFormUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val userDetails: UserProfileResponse? = null,
    val error: String? = null,
    val finishSaving: Boolean = false,
    val isFormPopulated: Boolean = false
)