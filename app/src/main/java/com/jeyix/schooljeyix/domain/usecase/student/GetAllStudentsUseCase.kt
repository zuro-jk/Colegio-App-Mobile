package com.jeyix.schooljeyix.domain.usecase.student

import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject

class GetAllStudentsUseCase @Inject constructor(
    private val repository: StudentRepository
) {
    suspend operator fun invoke(): Resource<List<StudentResponse>> {
        return repository.getAllStudents()
    }
}