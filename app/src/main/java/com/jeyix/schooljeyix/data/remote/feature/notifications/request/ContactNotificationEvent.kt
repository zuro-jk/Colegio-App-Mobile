package com.jeyix.schooljeyix.data.remote.feature.notifications.request

data class ContactNotificationEvent(
    val subject: String,
    val message: String,
    val userId: Long? = null,
    val actionUrl: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null
)

