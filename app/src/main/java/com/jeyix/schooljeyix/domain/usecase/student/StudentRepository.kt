package com.jeyix.schooljeyix.domain.usecase.student

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


}