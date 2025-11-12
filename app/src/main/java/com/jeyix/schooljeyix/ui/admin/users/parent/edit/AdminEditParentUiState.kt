package com.jeyix.schooljeyix.ui.admin.users.parent.edit

import com.jeyix.schooljeyix.data.remote.feature.parent.response.detail.ParentDetailResponse

data class AdminEditParentUiState(
    val isLoading: Boolean = true,
    val parent: ParentDetailResponse? = null,
    val isUpdateSuccessful: Boolean = false,
    val error: String? = null,
    val isFormPopulated: Boolean = false
)