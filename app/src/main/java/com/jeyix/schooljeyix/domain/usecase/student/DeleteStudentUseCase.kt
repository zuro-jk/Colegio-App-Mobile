package com.jeyix.schooljeyix.domain.usecase.student

import com.jeyix.schooljeyix.domain.repository.StudentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class DeleteStudentUseCase @Inject constructor(
    private val repository: StudentRepository
) {
    /**
     * Invoca el caso de uso para eliminar un estudiante.
     * @param studentId El ID del estudiante a eliminar.
     * @return Un Resource<Unit> que indica éxito o fracaso.
     */
    suspend operator fun invoke(studentId: Long): Resource<Unit> {
        return repository.deleteStudent(studentId)
    }
}