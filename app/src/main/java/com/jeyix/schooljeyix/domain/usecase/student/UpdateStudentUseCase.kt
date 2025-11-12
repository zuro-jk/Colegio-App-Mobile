package com.jeyix.schooljeyix.domain.usecase.student

import com.jeyix.schooljeyix.data.remote.feature.student.request.UpdateStudentRequest
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject

class UpdateStudentUseCase @Inject constructor(
    private val repository: StudentRepository
) {
    suspend operator fun invoke(studentId: Long, request: UpdateStudentRequest): Resource<StudentResponse> {
        return repository.updateStudent(studentId, request)
    }
}