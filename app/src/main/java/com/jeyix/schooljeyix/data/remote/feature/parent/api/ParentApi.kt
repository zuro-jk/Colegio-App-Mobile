package com.jeyix.schooljeyix.data.remote.feature.parent.api

import com.jeyix.schooljeyix.data.remote.core.ApiResponse
import com.jeyix.schooljeyix.data.remote.feature.parent.request.ParentRequest
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentDetailResponse
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ParentApi {

    @GET("parents")
    suspend fun getAllParents(): Response<ApiResponse<List<ParentResponse>>>

    @GET("parents/{id}")
    suspend fun getParentById(
        @Path("id") id: Long
    ): Response<ApiResponse<ParentResponse>>

    @GET("parents/{id}/details")
    suspend fun getParentByIdDetails(
        @Path("id") id: Long
    ): Response<ApiResponse<ParentDetailResponse>>

    @POST("parents")
    suspend fun addParent(
        @Body() parentRequest: ParentRequest
    ): Response<ApiResponse<ParentResponse>>

    @PUT("parents/{id}")
    suspend fun updateParentById(
        @Path("id") id: Long,
        @Body() parentRequest: ParentRequest
    ): Response<ApiResponse<ParentResponse>>

    @DELETE("parents/{id}")
    suspend fun deleteParentById(
        @Path("id") id: Long
    ): Response<ApiResponse<Unit>>


}