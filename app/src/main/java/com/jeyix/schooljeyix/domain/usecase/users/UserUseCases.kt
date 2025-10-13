package com.jeyix.schooljeyix.domain.usecase.users

import android.net.Uri
import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse
import com.jeyix.schooljeyix.data.remote.feature.users.request.ChangePasswordRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.DeviceTokenRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.UpdateProfileRequest
import com.jeyix.schooljeyix.data.remote.feature.users.response.UpdateProfileResponse
import com.jeyix.schooljeyix.data.remote.feature.users.response.UserSessionResponse
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject

class UserUseCases @Inject constructor(
    private val repository: UserRepository
) {

    suspend fun getMeData(): Resource<UserProfileResponse> {
        return repository.getMeData()
    }

    suspend fun getMySessions(): Resource<List<UserSessionResponse>> {
        return repository.getMySessions()
    }

    suspend fun getAllPersonalForAdmin(): Resource<List<UserProfileResponse>> {
        return repository.getAllPersonalForAdmin()
    }

    suspend fun getUserById(userId: Long): Resource<UserProfileResponse> {
        if (userId <= 0) {
            return Resource.Error("El ID de usuario no es válido.")
        }
        return repository.getUserById(userId)
    }

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Resource<Unit> {
        if (currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
            return Resource.Error("Todos los campos son obligatorios.")
        }
        if (newPassword.length < 8) {
            return Resource.Error("La nueva contraseña debe tener al menos 8 caracteres.")
        }
        if (newPassword != confirmPassword) {
            return Resource.Error("Las nuevas contraseñas no coinciden.")
        }
        if (currentPassword == newPassword) {
            return Resource.Error("La nueva contraseña no puede ser igual a la anterior.")
        }

        val requestDto = ChangePasswordRequest(
            currentPassword = currentPassword,
            newPassword = newPassword
        )

        return repository.changePassword(requestDto)
    }

    suspend fun updateDeviceToken(token: String): Resource<Unit> {
        if (token.isBlank()) {
            return Resource.Error("Token de dispositivo no válido.")
        }

        val request = DeviceTokenRequest(deviceToken = token)
        return repository.updateDeviceToken(request)
    }

    suspend fun updateProfile(request: UpdateProfileRequest): Resource<UpdateProfileResponse> {
        if (request.firstName.isBlank() || request.lastName.isBlank()) {
            return Resource.Error("El nombre y el apellido no pueden estar vacíos.")
        }
        return repository.updateProfile(request)
    }

    suspend fun updateProfileImage(imageUri: Uri): Resource<UserProfileResponse> {
        return repository.updateProfileImage(imageUri)
    }
}