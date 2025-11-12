package com.jeyix.schooljeyix.domain.usecase.grade

import jakarta.inject.Inject

class GetGradeByIdUseCase @Inject constructor(private val repo: GradeRepository) {
    suspend operator fun invoke(id: Long) = repo.getGradeById(id)
}