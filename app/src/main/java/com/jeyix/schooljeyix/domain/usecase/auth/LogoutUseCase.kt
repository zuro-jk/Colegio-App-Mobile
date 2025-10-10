package com.jeyix.schooljeyix.domain.usecase.auth

import android.util.Log
import com.jeyix.schooljeyix.data.local.datastore.UserPreferences
import com.jeyix.schooljeyix.data.remote.feature.auth.request.SessionLogoutRequest
import jakarta.inject.Inject
import kotlinx.coroutines.flow.firstOrNull

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val userPreferences: UserPreferences
) {
    suspend operator fun invoke() {
        try {
            val sessionId = userPreferences.sessionId.firstOrNull()

            if (sessionId != null && sessionId != 0) {
                val result = repository.logout(SessionLogoutRequest(sessionId.toLong()))
                Log.d("LogoutUseCase", "Resultado del logout en backend: $result")
            }
        } catch (e: Exception) {
            Log.e("LogoutUseCase", "Error al notificar logout al backend: ${e.message}")
        } finally {
            userPreferences.clear()
        }
    }
}