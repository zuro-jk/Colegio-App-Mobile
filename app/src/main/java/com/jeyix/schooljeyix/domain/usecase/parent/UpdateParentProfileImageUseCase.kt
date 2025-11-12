package com.jeyix.schooljeyix.domain.usecase.parent

import android.net.Uri
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.domain.repository.ParentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class UpdateParentProfileImageUseCase @Inject constructor(
   private val repository: ParentRepository
) {
    suspend operator fun invoke(parentId: Long, imageUri: Uri): Resource<ParentResponse> {
        if (parentId <= 0) {
            return Resource.Error("ID de estudiante no válido")
        }
        return repository.updateParentProfileImage(parentId, imageUri)
    }
}