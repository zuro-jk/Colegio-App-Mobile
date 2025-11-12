package com.jeyix.schooljeyix.data.remote.feature.section.api

import com.jeyix.schooljeyix.data.remote.core.ApiResponse
import com.jeyix.schooljeyix.data.remote.feature.section.request.SectionRequest
import com.jeyix.schooljeyix.data.remote.feature.section.response.SectionResponse
import retrofit2.Response
import retrofit2.http.*

interface SectionApi {

    @POST("sections")
    suspend fun createSection(@Body request: SectionRequest): Response<ApiResponse<SectionResponse>>

    @GET("sections")
    suspend fun getAllSections(): Response<ApiResponse<List<SectionResponse>>>

    @GET("sections/by-grade/{gradeId}")
    suspend fun getSectionsByGradeId(
        @Path("gradeId") gradeId: Long
    ): Response<ApiResponse<List<SectionResponse>>>

    @GET("sections/{id}")
    suspend fun getSectionById(@Path("id") id: Long): Response<ApiResponse<SectionResponse>>

    @PUT("sections/{id}")
    suspend fun updateSection(
        @Path("id") id: Long,
        @Body request: SectionRequest
    ): Response<ApiResponse<SectionResponse>>

    @DELETE("sections/{id}")
    suspend fun deleteSection(@Path("id") id: Long): Response<ApiResponse<Void>>
}