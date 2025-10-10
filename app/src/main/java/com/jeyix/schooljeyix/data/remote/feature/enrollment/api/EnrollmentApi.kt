package com.jeyix.schooljeyix.data.remote.feature.enrollment.api

import com.jeyix.schooljeyix.data.remote.core.ApiResponse
import com.jeyix.schooljeyix.data.remote.feature.enrollment.request.EnrollmentRequest
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EnrollmentApi {

    @GET("enrollments")
    suspend fun getAllEnrollments(): Response<ApiResponse<List<EnrollmentResponse>>>

    @GET("enrollments/{id}")
    suspend fun getEnrollmentById(
        @Path("id") enrollmentId: Long
    ): Response<ApiResponse<EnrollmentResponse>>

    @GET("enrollments/my-enrollments")
    suspend fun getAllEnrollmentsAuth(): Response<ApiResponse<List<EnrollmentResponse>>>

    @POST("enrollments")
    suspend fun createEnrollment(
        @Body enrollmentRequest: EnrollmentRequest
    ): Response<ApiResponse<EnrollmentResponse>>
}