package com.jeyix.schooljeyix.domain.usecase.section

import com.jeyix.schooljeyix.data.remote.feature.section.request.SectionRequest
import com.jeyix.schooljeyix.domain.repository.SectionRepository
import javax.inject.Inject

class UpdateSectionUseCase @Inject constructor(private val repo: SectionRepository) {
    suspend operator fun invoke(id: Long, request: SectionRequest) = repo.updateSection(id, request)
}