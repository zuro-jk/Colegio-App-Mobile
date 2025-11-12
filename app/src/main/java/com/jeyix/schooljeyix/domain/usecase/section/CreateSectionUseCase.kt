package com.jeyix.schooljeyix.domain.usecase.section

import com.jeyix.schooljeyix.data.remote.feature.section.request.SectionRequest
import jakarta.inject.Inject

class CreateSectionUseCase @Inject constructor(private val repo: SectionRepository) {
    suspend operator fun invoke(request: SectionRequest) = repo.createSection(request)
}