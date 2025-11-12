package com.jeyix.schooljeyix.domain.repository

import com.jeyix.schooljeyix.data.remote.feature.section.request.SectionRequest
import com.jeyix.schooljeyix.data.remote.feature.section.response.SectionResponse
import com.jeyix.schooljeyix.domain.util.Resource

interface SectionRepository {
    suspend fun createSection(request: SectionRequest): Resource<SectionResponse>
    suspend fun getAllSections(): Resource<List<SectionResponse>>
    suspend fun getSectionsByGradeId(gradeId: Long): Resource<List<SectionResponse>>
    suspend fun getSectionById(id: Long): Resource<SectionResponse>
    suspend fun updateSection(id: Long, request: SectionRequest): Resource<SectionResponse>
    suspend fun deleteSection(id: Long): Resource<Unit>
}