package com.jeyix.schooljeyix.ui.parent.notifications

import com.jeyix.schooljeyix.domain.model.Announcement

data class NotificationsUiState(
    val isLoading: Boolean = true,
    val notifications: List<Announcement> = emptyList(),
    val error: String? = null
)