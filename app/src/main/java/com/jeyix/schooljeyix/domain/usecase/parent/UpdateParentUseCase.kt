package com.jeyix.schooljeyix.domain.usecase.parent

import com.jeyix.schooljeyix.data.remote.feature.parent.request.UpdateParentRequest
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject

class UpdateParentUseCase @Inject constructor(
    private val repository: ParentRepository
) {
    suspend operator fun invoke(id: Long, request: UpdateParentRequest): Resource<ParentResponse> {
        if (id <= 0) {
            return Resource.Error("El ID del padre no es válido.")
        }
        return repository.updateParent(id, request)
    }
}