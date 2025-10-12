package com.jeyix.schooljeyix.domain.usecase.parent

import com.jeyix.schooljeyix.data.remote.feature.parent.request.ParentRequest
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.domain.util.Resource

interface ParentRepository {

    suspend fun getAllParents(): Resource<List<ParentResponse>>

    suspend fun getParentById(
        id: Long
    ): Resource<ParentResponse>

    suspend fun addParent(
        parentRequest: ParentRequest
    ): Resource<ParentResponse>

    suspend fun updateParentById(
        id: Long,
        parentRequest: ParentRequest
    ): Resource<ParentResponse>

    suspend fun deleteParentById(
        id: Long
    ): Resource<Unit>

}