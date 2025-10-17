package com.jeyix.schooljeyix.ui.admin.users.parent

import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse

data class AdminParentListUiState(
    val isLoading: Boolean = true,
    val parents: List<ParentResponse> = emptyList(),
    val error: String? = null
)