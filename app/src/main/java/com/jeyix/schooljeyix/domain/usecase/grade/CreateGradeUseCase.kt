package com.jeyix.schooljeyix.domain.usecase.grade

import com.jeyix.schooljeyix.data.remote.feature.grade.request.GradeRequest
import jakarta.inject.Inject

class CreateGradeUseCase @Inject constructor(private val repo: GradeRepository) {
    suspend operator fun invoke(request: GradeRequest) = repo.createGrade(request)
}