package com.jeyix.schooljeyix.domain.repository

import com.jeyix.schooljeyix.data.remote.feature.notifications.request.AnnouncementRequest
import com.jeyix.schooljeyix.data.remote.feature.notifications.request.ContactNotificationEvent
import com.jeyix.schooljeyix.data.remote.feature.notifications.response.NotificationResponse
import com.jeyix.schooljeyix.domain.util.Resource

interface NotificationRepository {
    suspend fun sendContact(contactRequest: ContactNotificationEvent): Resource<String>

    suspend fun sendAnnouncement(announcementRequest: AnnouncementRequest): Resource<Unit>
    suspend fun getAllNotifications(): Resource<List<NotificationResponse>>

}