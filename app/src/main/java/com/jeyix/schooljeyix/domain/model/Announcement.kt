package com.jeyix.schooljeyix.domain.model

data class Announcement(
    val id: Long,
    val title: String,
    val body: String,
    val target: String,
    val timestamp: String,
    val type: AnnouncementType,
    val isUnread: Boolean
)

enum class AnnouncementType {
    URGENT, INFO, PAYMENT
}