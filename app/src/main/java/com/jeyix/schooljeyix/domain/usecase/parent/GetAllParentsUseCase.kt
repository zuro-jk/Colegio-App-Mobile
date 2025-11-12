package com.jeyix.schooljeyix.domain.usecase.parent

import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject

class GetAllParentsUseCase @Inject constructor(
    private val repository: ParentRepository
) {
    suspend operator fun invoke(): Resource<List<ParentResponse>> {
        return repository.getAllParents()
    }
}