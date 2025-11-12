package com.jeyix.schooljeyix.domain.usecase.parent

import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.domain.repository.ParentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class GetParentByIdUseCase @Inject constructor(
    private val repository: ParentRepository
) {
    suspend operator fun invoke(id: Long): Resource<ParentResponse> {
        if (id <= 0) {
            return Resource.Error("El ID del padre no es válido.")
        }
        return repository.getParentById(id)
    }
}