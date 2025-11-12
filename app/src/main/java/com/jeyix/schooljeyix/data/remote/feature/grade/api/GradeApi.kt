package com.jeyix.schooljeyix.data.remote.feature.grade.api

import com.jeyix.schooljeyix.data.remote.core.ApiResponse
import com.jeyix.schooljeyix.data.remote.feature.grade.request.GradeRequest
import com.jeyix.schooljeyix.data.remote.feature.grade.response.GradeDetailResponse
import com.jeyix.schooljeyix.data.remote.feature.grade.response.GradeResponse
import retrofit2.Response
import retrofit2.http.*

interface GradeApi {

    @POST("grades")
    suspend fun createGrade(@Body request: GradeRequest): Response<ApiResponse<GradeResponse>>

    @GET("grades")
    suspend fun getAllGrades(): Response<ApiResponse<List<GradeResponse>>>

    @GET("grades/{id}")
    suspend fun getGradeById(@Path("id") id: Long): Response<ApiResponse<GradeDetailResponse>>

    @PUT("grades/{id}")
    suspend fun updateGrade(
        @Path("id") id: Long,
        @Body request: GradeRequest
    ): Response<ApiResponse<GradeResponse>>

    @DELETE("grades/{id}")
    suspend fun deleteGrade(@Path("id") id: Long): Response<ApiResponse<Void>>
}