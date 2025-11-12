package com.jeyix.schooljeyix.data.remote.feature.parent.api

import com.jeyix.schooljeyix.data.remote.core.ApiResponse
import com.jeyix.schooljeyix.data.remote.feature.parent.request.CreateParentRequest
import com.jeyix.schooljeyix.data.remote.feature.parent.request.UpdateParentRequest
import com.jeyix.schooljeyix.data.remote.feature.parent.response.detail.ParentDetailResponse
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ParentApi {

    @GET("parents")
    suspend fun getAllParents(): Response<ApiResponse<List<ParentResponse>>>

    @GET("parents/{id}")
    suspend fun getParentById(@Path("id") id: Long): Response<ApiResponse<ParentResponse>>

    @GET("parents/{id}/details")
    suspend fun getParentByIdDetails(@Path("id") id: Long): Response<ApiResponse<ParentDetailResponse>>

    @POST("parents")
    suspend fun createParent(
        @Body request: CreateParentRequest
    ): Response<ApiResponse<ParentResponse>>

    @Multipart
    @POST("parents/{parentId}/profile-image")
    suspend fun uploadParentProfileImage(
        @Path("parentId") parentId: Long,
        @Part file: MultipartBody.Part
    ): Response<ApiResponse<ParentResponse>>


    @PUT("parents/{id}")
    suspend fun updateParent(
        @Path("id") id: Long,
        @Body request: UpdateParentRequest
    ): Response<ApiResponse<ParentResponse>>

    @DELETE("parents/{id}")
    suspend fun deleteParent(
        @Path("id") id: Long
    ): Response<ApiResponse<Void>>
}