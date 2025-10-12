package com.jeyix.schooljeyix.data.remote.feature.users.api

import com.jeyix.schooljeyix.data.remote.core.ApiResponse
import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse
import com.jeyix.schooljeyix.data.remote.feature.users.request.ChangePasswordRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.UpdateProfileRequest
import com.jeyix.schooljeyix.data.remote.feature.users.response.UpdateProfileResponse
import com.jeyix.schooljeyix.data.remote.feature.users.response.UserSessionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApi {

    @GET("users/me")
    suspend fun getMeData(): Response<ApiResponse<UserProfileResponse>>

    @GET("users/session")
    suspend fun getMySessions(): Response<ApiResponse<List<UserSessionResponse>>>

    @GET("users/admin")
    suspend fun getAllPersonalForAdmin(): Response<ApiResponse<List<UserProfileResponse>>>

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") userId: Long
    ): Response<ApiResponse<UserProfileResponse>>

    @PUT("users/change-password")
    suspend fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<ApiResponse<Unit>>

    @PUT("users/update-profile")
    suspend fun updateProfile(
        @Body updateProfileRequest: UpdateProfileRequest
    ): Response<ApiResponse<UpdateProfileResponse>>

    @Multipart
    @PUT("users/profile-image")
    suspend fun updateProfileImage(
        @Part file: MultipartBody.Part
    ): Response<ApiResponse<UserProfileResponse>>

}