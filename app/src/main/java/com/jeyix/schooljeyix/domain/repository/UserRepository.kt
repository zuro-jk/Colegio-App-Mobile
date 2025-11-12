package com.jeyix.schooljeyix.domain.repository

import android.net.Uri
import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse
import com.jeyix.schooljeyix.data.remote.feature.users.request.ChangePasswordRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.CreateAdminRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.DeviceTokenRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.UpdateProfileRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.UpdateUserRequest
import com.jeyix.schooljeyix.data.remote.feature.users.response.UpdateProfileResponse
import com.jeyix.schooljeyix.data.remote.feature.users.response.UserSessionResponse
import com.jeyix.schooljeyix.domain.util.Resource

interface UserRepository {

    suspend fun getMeData(): Resource<UserProfileResponse>
    suspend fun getMySessions(): Resource<List<UserSessionResponse>>
    suspend fun getAllPersonalForAdmin(): Resource<List<UserProfileResponse>>
    suspend fun getUserById(userId: Long): Resource<UserProfileResponse>
    suspend fun updateDeviceToken(deviceTokenRequest: DeviceTokenRequest): Resource<Unit>
    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Resource<Unit>
    suspend fun updateProfile(updateProfileRequest: UpdateProfileRequest): Resource<UpdateProfileResponse>
    suspend fun updateProfileImage(imageUri: Uri): Resource<UserProfileResponse>

    // --- MÉTODOS DE ADMIN NUEVOS AÑADIDOS ---

    /**
     * Endpoint para que un Admin cree un nuevo usuario (Admin, Profesor, etc.)
     */
    suspend fun createAdminUser(
        request: CreateAdminRequest
    ): Resource<UserProfileResponse>

    /**
     * Endpoint para que un Admin actualice los datos JSON de cualquier usuario
     */
    suspend fun updateUserById(
        userId: Long,
        request: UpdateUserRequest
    ): Resource<UserProfileResponse>

    /**
     * Endpoint para que un Admin actualice la imagen de perfil de cualquier usuario
     */
    suspend fun updateUserImageById(
        userId: Long,
        imageUri: Uri
    ): Resource<UserProfileResponse>
}