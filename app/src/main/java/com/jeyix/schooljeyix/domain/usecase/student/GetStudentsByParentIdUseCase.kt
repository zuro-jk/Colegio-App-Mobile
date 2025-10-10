package com.jeyix.schooljeyix.domain.usecase.student

import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject

class GetStudentsByParentIdUseCase @Inject constructor(
    private val repository: StudentRepository
) {
    suspend operator fun invoke(parentId: Long): Resource<List<StudentResponse>> {
        if (parentId <= 0) {
            return Resource.Error("El ID del padre no es válido.")
        }
        return repository.getStudentByParentId(parentId)
    }
}