package com.jeyix.schooljeyix.domain.usecase.student

import android.net.Uri
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.domain.repository.StudentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class UpdateStudentProfileImageUseCase @Inject constructor(
    private val repository: StudentRepository
) {
    suspend operator fun invoke(studentId: Long, imageUri: Uri): Resource<StudentResponse> {
        if (studentId <= 0) {
            return Resource.Error("ID de estudiante no válido")
        }
        return repository.updateStudentProfileImage(studentId, imageUri)
    }
}