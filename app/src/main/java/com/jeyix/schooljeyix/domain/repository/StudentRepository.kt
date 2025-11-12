package com.jeyix.schooljeyix.domain.repository

import android.net.Uri
import com.jeyix.schooljeyix.data.remote.feature.student.request.CreateStudentRequest
import com.jeyix.schooljeyix.data.remote.feature.student.request.UpdateStudentRequest
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.domain.util.Resource

interface StudentRepository {

    suspend fun getAllStudents(): Resource<List<StudentResponse>>

    suspend fun getStudentById(
        studentId: Long
    ): Resource<StudentResponse>

    suspend fun getStudentByParentId(
        parentId: Long
    ): Resource<List<StudentResponse>>

    suspend fun getMyStudents(): Resource<List<StudentResponse>>

    /**
     * Crea un nuevo estudiante (User + Student)
     */
    suspend fun createStudent(
        request: CreateStudentRequest
    ): Resource<StudentResponse>

    /**
     * Actualiza un estudiante existente
     */
    suspend fun updateStudent(
        studentId: Long,
        request: UpdateStudentRequest
    ): Resource<StudentResponse>

    /**
     * Elimina un estudiante
     * Devuelve Resource<Unit> para indicar éxito/fracaso de la operación.
     */
    suspend fun deleteStudent(
        studentId: Long
    ): Resource<Unit>

    suspend fun updateStudentProfileImage(
        studentId: Long,
        imageUri: Uri
    ): Resource<StudentResponse>

}