package com.jeyix.schooljeyix.domain.usecase.grade

import com.jeyix.schooljeyix.data.remote.feature.grade.request.GradeRequest
import jakarta.inject.Inject

class UpdateGradeUseCase @Inject constructor(private val repo: GradeRepository) {
    suspend operator fun invoke(id: Long, request: GradeRequest) = repo.updateGrade(id, request)
}