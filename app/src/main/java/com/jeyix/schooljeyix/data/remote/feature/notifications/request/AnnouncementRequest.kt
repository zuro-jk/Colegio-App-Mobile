package com.jeyix.schooljeyix.data.remote.feature.notifications.request

data class AnnouncementRequest(
    val title: String,
    val body: String,
    val targetRoles: List<String>
)
