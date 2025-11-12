package com.jeyix.schooljeyix.domain.repository

import android.net.Uri
import com.jeyix.schooljeyix.data.remote.feature.parent.request.CreateParentRequest
import com.jeyix.schooljeyix.data.remote.feature.parent.request.UpdateParentRequest
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.data.remote.feature.parent.response.detail.ParentDetailResponse
import com.jeyix.schooljeyix.domain.util.Resource

interface ParentRepository {

    suspend fun getAllParents(): Resource<List<ParentResponse>>

    suspend fun getParentById(
        id: Long
    ): Resource<ParentResponse>

    suspend fun getParentByIdDetails(
        id: Long
    ): Resource<ParentDetailResponse>

    // --- MÉTODOS CORREGIDOS ---
    // Renombrado y usa el DTO correcto
    suspend fun createParent(
        request: CreateParentRequest
    ): Resource<ParentResponse>

    suspend fun updateParentProfileImage(
        parentId: Long,
        imageUri: Uri
    ): Resource<ParentResponse>

    // Renombrado y usa el DTO correcto
    suspend fun updateParent(
        id: Long,
        request: UpdateParentRequest
    ): Resource<ParentResponse>

    // Renombrado por consistencia
    suspend fun deleteParent(
        id: Long
    ): Resource<Unit>


}