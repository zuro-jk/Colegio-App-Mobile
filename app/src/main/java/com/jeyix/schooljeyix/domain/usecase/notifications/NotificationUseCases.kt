package com.jeyix.schooljeyix.domain.usecase.notifications

import com.jeyix.schooljeyix.data.remote.feature.notifications.request.AnnouncementRequest
import com.jeyix.schooljeyix.data.remote.feature.notifications.request.ContactNotificationEvent
import com.jeyix.schooljeyix.data.remote.feature.notifications.response.NotificationResponse
import com.jeyix.schooljeyix.domain.model.Announcement
import com.jeyix.schooljeyix.domain.model.AnnouncementType
import com.jeyix.schooljeyix.domain.repository.NotificationRepository
import com.jeyix.schooljeyix.domain.util.Resource
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class NotificationUseCases @Inject constructor(
    private val repository: NotificationRepository
){

    suspend fun getAnnouncements(): Resource<List<Announcement>> {
        return when(val result = repository.getAllNotifications()) {
            is Resource.Success -> {
                val apiList = result.data ?: emptyList()

                val domainList = apiList.map { dto ->
                    mapToDomain(dto)
                }.sortedByDescending { it.id }

                Resource.Success(domainList)
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error desconocido")
            }
            is Resource.Loading -> Resource.Loading()
        }
    }

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

    private fun mapToDomain(dto: NotificationResponse): Announcement {
        val type = when {
            dto.title.contains("Pago", true) || dto.title.contains("Deuda", true) -> AnnouncementType.PAYMENT
            dto.title.contains("Urgente", true) || dto.title.contains("Importante", true) -> AnnouncementType.URGENT
            else -> AnnouncementType.INFO
        }

        val formattedDate = try {
            val parsedDate = LocalDateTime.parse(dto.sentAt)
            val formatter = DateTimeFormatter.ofPattern("dd MMM, hh:mm a", Locale("es", "ES"))
            parsedDate.format(formatter)
        } catch (e: Exception) {
            dto.sentAt
        }

        return Announcement(
            id = dto.id,
            title = dto.title,
            body = dto.body,
            target = dto.recipientName ?: "Todos",
            timestamp = formattedDate,
            type = type,
            isUnread = false
        )
    }
}