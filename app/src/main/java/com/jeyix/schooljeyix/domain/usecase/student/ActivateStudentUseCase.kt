package com.jeyix.schooljeyix.domain.usecase.student

import com.jeyix.schooljeyix.domain.repository.StudentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class ActivateStudentUseCase @Inject constructor(
    private val repository: StudentRepository
) {
    suspend operator fun invoke(studentId: Long): Resource<Unit> {
        return repository.activateStudent(studentId)
    }
}