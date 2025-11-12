package com.jeyix.schooljeyix.domain.usecase.grade

import com.jeyix.schooljeyix.data.remote.feature.grade.request.GradeRequest
import com.jeyix.schooljeyix.domain.repository.GradeRepository
import javax.inject.Inject

class CreateGradeUseCase @Inject constructor(private val repo: GradeRepository) {
    suspend operator fun invoke(request: GradeRequest) = repo.createGrade(request)
}