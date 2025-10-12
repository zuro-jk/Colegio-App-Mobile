package com.jeyix.schooljeyix.domain.usecase.users

import android.net.Uri
import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse
import com.jeyix.schooljeyix.data.remote.feature.users.request.ChangePasswordRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.UpdateProfileRequest
import com.jeyix.schooljeyix.data.remote.feature.users.response.UpdateProfileResponse
import com.jeyix.schooljeyix.data.remote.feature.users.response.UserSessionResponse
import com.jeyix.schooljeyix.domain.util.Resource

interface UserRepository {

    suspend fun getMeData(): Resource<UserProfileResponse>

    suspend fun getMySessions(): Resource<List<UserSessionResponse>>

    suspend fun getAllPersonalForAdmin(): Resource<List<UserProfileResponse>>

    suspend fun getUserById(
        userId: Long
    ): Resource<UserProfileResponse>

    suspend fun changePassword(
        changePasswordRequest: ChangePasswordRequest
    ): Resource<Unit>

    suspend fun updateProfile(
        updateProfileRequest: UpdateProfileRequest
    ): Resource<UpdateProfileResponse>

    suspend fun updateProfileImage(
        imageUri: Uri
    ): Resource<UserProfileResponse>
}