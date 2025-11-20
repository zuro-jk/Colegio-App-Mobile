package com.jeyix.schooljeyix.domain.usecase.notifications

import com.jeyix.schooljeyix.data.remote.feature.notifications.request.AnnouncementRequest
import com.jeyix.schooljeyix.data.remote.feature.notifications.request.ContactNotificationEvent
import com.jeyix.schooljeyix.domain.repository.NotificationRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class NotificationUseCases @Inject constructor(
    private val repository: NotificationRepository
){


    suspend fun sendAnnouncement(
        title: String,
        body: String,
        targetRoles: List<String>
    ): Resource<Unit> {
        if (title.isBlank() || body.isBlank()) {
            return Resource.Error("El título y el cuerpo del anuncio no pueden estar vacíos.")
        }
        if (targetRoles.isEmpty()) {
            return Resource.Error("Debe seleccionar al menos un grupo de destinatarios (ej: ['ROLE_PARENT']).")
        }

        val request = AnnouncementRequest(
            title = title,
            body = body,
            targetRoles = targetRoles
        )

        return repository.sendAnnouncement(request)
    }


    suspend fun sendContact(
        subject: String,
        message: String,
    ): Resource<String> {
        if (subject.isBlank() || message.isBlank()) {
            return Resource.Error("El asunto y el mensaje son obligatorios.")
        }
        if (message.length < 10) {
            return Resource.Error("El mensaje debe tener al menos 10 caracteres para ser enviado.")
        }

        val request = ContactNotificationEvent(
            subject = subject,
            message = message
        )

        return repository.sendContact(request)
    }
}