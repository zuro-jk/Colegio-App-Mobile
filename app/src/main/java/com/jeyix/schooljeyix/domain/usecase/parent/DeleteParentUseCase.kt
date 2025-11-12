package com.jeyix.schooljeyix.domain.usecase.parent

import com.jeyix.schooljeyix.domain.repository.ParentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class DeleteParentUseCase @Inject constructor(
    private val repository: ParentRepository
) {
    suspend operator fun invoke(id: Long): Resource<Unit> {
        if (id <= 0) {
            return Resource.Error("El ID del padre no es válido.")
        }
        return repository.deleteParent(id)
    }
}