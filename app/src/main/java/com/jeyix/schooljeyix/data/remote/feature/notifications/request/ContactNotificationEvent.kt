package com.jeyix.schooljeyix.data.remote.feature.notifications.request

data class ContactNotificationEvent(
    val userId: Long,
    val subject: String,
    val message: String,
    val actionUrl: String,
    val name: String,
    val email: String,
    val phone: String
)

