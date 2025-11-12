package com.jeyix.schooljeyix.data.repository.notifications

import com.jeyix.schooljeyix.data.remote.feature.notifications.api.NotificationApi
import com.jeyix.schooljeyix.data.remote.feature.notifications.request.AnnouncementRequest
import com.jeyix.schooljeyix.data.remote.feature.notifications.request.ContactNotificationEvent
import com.jeyix.schooljeyix.domain.repository.NotificationRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationApi
): NotificationRepository {


    override suspend fun sendContact(contactRequest: ContactNotificationEvent): Resource<String> {
        return try {
            val response = api.sendContact(contactRequest)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.message)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al enviar el mensaje.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }


    override suspend fun sendAnnouncement(announcementRequest: AnnouncementRequest): Resource<Unit> {
        return try {
            val response = api.sendAnnouncement(announcementRequest)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al enviar el anuncio.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }


}