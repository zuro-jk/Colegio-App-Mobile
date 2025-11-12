package com.jeyix.schooljeyix.domain.usecase.section

import jakarta.inject.Inject

class DeleteSectionUseCase @Inject constructor(private val repo: SectionRepository) {
    suspend operator fun invoke(id: Long) = repo.deleteSection(id)
}