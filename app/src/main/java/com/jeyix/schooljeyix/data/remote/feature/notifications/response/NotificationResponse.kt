package com.jeyix.schooljeyix.data.remote.feature.notifications.response

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    val id: Long,
    val title: String,
    val body: String,
    val sentAt: String,
    val status: String,
    val recipientName: String?
)