package com.jeyix.schooljeyix.data.remote.feature.student.api

import com.jeyix.schooljeyix.data.remote.core.ApiResponse
import com.jeyix.schooljeyix.data.remote.feature.student.request.CreateStudentRequest
import com.jeyix.schooljeyix.data.remote.feature.student.request.UpdateStudentRequest
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

    /**
     * Crea un nuevo estudiante (User + Student)
     * Corresponde a tu @PostMapping
     */
    @POST("students")
    suspend fun createStudent(
        @Body request: CreateStudentRequest
    ): Response<ApiResponse<StudentResponse>>

    /**
     * Sube y asigna una imagen de perfil a un estudiante (Admin)
     */
    @Multipart
    @POST("students/{id}/profile-image")
    suspend fun uploadStudentProfileImage(
        @Path("id") studentId: Long,
        @Part file: MultipartBody.Part // Así se envía un archivo
    ): Response<ApiResponse<StudentResponse>>

    /**
     * Actualiza un estudiante existente
     * Corresponde a tu @PutMapping("/{id}")
     */
    @PUT("students/{id}")
    suspend fun updateStudent(
        @Path("id") studentId: Long,
        @Body request: UpdateStudentRequest
    ): Response<ApiResponse<StudentResponse>>

    /**
     * Elimina un estudiante
     * Corresponde a tu @DeleteMapping("/{id}")
     */
    @DELETE("students/{id}")
    suspend fun deleteStudent(
        @Path("id") studentId: Long
    ): Response<ApiResponse<Void>>


}