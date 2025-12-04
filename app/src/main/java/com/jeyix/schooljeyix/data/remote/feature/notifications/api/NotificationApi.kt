package com.jeyix.schooljeyix.data.remote.feature.notifications.api

import com.jeyix.schooljeyix.data.remote.core.ApiResponse
import com.jeyix.schooljeyix.data.remote.feature.notifications.request.AnnouncementRequest
import com.jeyix.schooljeyix.data.remote.feature.notifications.request.ContactNotificationEvent
import com.jeyix.schooljeyix.data.remote.feature.notifications.response.NotificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationApi {

    @GET("notification")
    suspend fun getAllNotifications(): Response<ApiResponse<List<NotificationResponse>>>

    @POST("notification")
    suspend fun sendContact(
        @Body() contactNotificationEvent: ContactNotificationEvent
    ): Response<ApiResponse<String>>

    @POST("notification/announcement")
    suspend fun sendAnnouncement(
        @Body() announcementRequest: AnnouncementRequest
    ): Response<ApiResponse<Unit>>

}
