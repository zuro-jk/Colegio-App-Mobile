package com.jeyix.schooljeyix.data.remote.feature.student.api

import com.jeyix.schooljeyix.data.remote.core.ApiResponse
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StudentApi {

    @GET("students")
    suspend fun getAllStudents(): Response<ApiResponse<List<StudentResponse>>>

    @GET("students/{id}")
    suspend fun getStudentById(
        @Path("id") studentId: Long
    ): Response<ApiResponse<StudentResponse>>

    @GET("students/parent/{parentId}")
    suspend fun getStudentByParentId(
        @Path("parentId") parentId: Long
    ): Response<ApiResponse<List<StudentResponse>>>

    @GET("students/my-children")
    suspend fun getMyStudents(): Response<ApiResponse<List<StudentResponse>>>


}