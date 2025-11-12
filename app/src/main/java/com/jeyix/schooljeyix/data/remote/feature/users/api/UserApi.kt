package com.jeyix.schooljeyix.data.remote.feature.users.api

import com.jeyix.schooljeyix.data.remote.core.ApiResponse
import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse
import com.jeyix.schooljeyix.data.remote.feature.users.request.ChangePasswordRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.CreateAdminRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.DeviceTokenRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.UpdateProfileRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.UpdateUserRequest
import com.jeyix.schooljeyix.data.remote.feature.users.response.UpdateProfileResponse
import com.jeyix.schooljeyix.data.remote.feature.users.response.UserSessionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApi {

    @GET("users/me")
    suspend fun getMeData(): Response<ApiResponse<UserProfileResponse>>

    @GET("users/sessions")
    suspend fun getMySessions(): Response<ApiResponse<List<UserSessionResponse>>>

    @GET("users/admins")
    suspend fun getAllPersonalForAdmin(): Response<ApiResponse<List<UserProfileResponse>>>

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") userId: Long
    ): Response<ApiResponse<UserProfileResponse>>

    @POST("users/device-token")
    suspend fun updateDeviceToken(
        @Body deviceTokenRequest: DeviceTokenRequest
    ): Response<ApiResponse<Unit>>

    @PUT("users/change-password")
    suspend fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<ApiResponse<Unit>>

    @PUT("users/update-profile")
    suspend fun updateProfile(
        @Body updateProfileRequest: UpdateProfileRequest
    ): Response<ApiResponse<UpdateProfileResponse>>

    @Multipart
    @POST("users/profile-image")
    suspend fun updateProfileImage(
        @Part file: MultipartBody.Part
    ): Response<ApiResponse<UserProfileResponse>>

    /**
     * Endpoint para que un Admin cree un nuevo usuario (Admin, Profesor, etc.)
     */
    @POST("users")
    suspend fun createAdminUser(
        @Body request: CreateAdminRequest
    ): Response<ApiResponse<UserProfileResponse>>

    /**
     * Endpoint para que un Admin actualice los datos JSON de cualquier usuario
     */
    @PUT("users/{id}")
    suspend fun updateUserById(
        @Path("id") userId: Long,
        @Body request: UpdateUserRequest
    ): Response<ApiResponse<UserProfileResponse>>

    /**
     * Endpoint para que un Admin actualice la imagen de perfil de cualquier usuario
     */
    @Multipart
    @POST("users/{id}/profile-image")
    suspend fun updateUserImageById(
        @Path("id") userId: Long,
        @Part file: MultipartBody.Part
    ): Response<ApiResponse<UserProfileResponse>>

}