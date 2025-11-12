package com.jeyix.schooljeyix.domain.usecase.section

import jakarta.inject.Inject

class GetSectionsByGradeIdUseCase @Inject constructor(private val repo: SectionRepository) {
    suspend operator fun invoke(gradeId: Long) = repo.getSectionsByGradeId(gradeId)
}