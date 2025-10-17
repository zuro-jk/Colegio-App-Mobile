package com.jeyix.schooljeyix.domain.usecase.parent

import com.jeyix.schooljeyix.data.remote.feature.parent.request.ParentRequest
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentDetailResponse
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject

class ParentUseCases @Inject constructor(
    private val repository: ParentRepository
) {

    suspend fun getAllParents(): Resource<List<ParentResponse>> {
        return repository.getAllParents()
    }

    suspend fun getParentById(id: Long): Resource<ParentResponse> {
        if (id <= 0) {
            return Resource.Error("El ID del padre no es válido.")
        }
        return repository.getParentById(id)
    }

    suspend fun getParentByIdDetails(id: Long): Resource<ParentDetailResponse> {
        if (id <= 0) {
            return Resource.Error("El ID del padre no es válido.")
        }
        return repository.getParentByIdDetails(id)
    }

    suspend fun addParent(request: ParentRequest): Resource<ParentResponse> {
        return repository.addParent(request)
    }

    suspend fun updateParent(id: Long, request: ParentRequest): Resource<ParentResponse> {
        if (id <= 0) {
            return Resource.Error("El ID del padre no es válido.")
        }
        return repository.updateParentById(id, request)
    }

    suspend fun deleteParent(id: Long): Resource<Unit> {
        if (id <= 0) {
            return Resource.Error("El ID del padre no es válido.")
        }
        return repository.deleteParentById(id)
    }
}