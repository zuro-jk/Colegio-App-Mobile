package com.jeyix.schooljeyix.domain.usecase.student

import com.jeyix.schooljeyix.data.remote.feature.student.request.CreateStudentRequest
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.domain.repository.StudentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class CreateStudentUseCase @Inject constructor(
    private val repository: StudentRepository
) {
    /**
     * Invoca el caso de uso para crear un nuevo estudiante.
     * @param request El DTO con toda la información del estudiante y usuario a crear.
     * @return Un Resource con el StudentResponse del estudiante creado.
     */
    suspend operator fun invoke(request: CreateStudentRequest): Resource<StudentResponse> {
        return repository.createStudent(request)
    }
}