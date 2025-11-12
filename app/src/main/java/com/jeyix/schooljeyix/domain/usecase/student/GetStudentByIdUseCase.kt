package com.jeyix.schooljeyix.domain.usecase.student

import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.domain.repository.StudentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class GetStudentByIdUseCase @Inject constructor(
    private val repository: StudentRepository
) {
    suspend operator fun invoke(studentId: Long): Resource<StudentResponse> {
        if (studentId <= 0) {
            return Resource.Error("El ID del estudiante no es válido.")
        }
        return repository.getStudentById(studentId)
    }
}