package com.jeyix.schooljeyix.domain.repository

import com.jeyix.schooljeyix.data.remote.feature.grade.request.GradeRequest
import com.jeyix.schooljeyix.data.remote.feature.grade.response.GradeDetailResponse
import com.jeyix.schooljeyix.data.remote.feature.grade.response.GradeResponse
import com.jeyix.schooljeyix.domain.util.Resource

interface GradeRepository {
    suspend fun createGrade(request: GradeRequest): Resource<GradeResponse>
    suspend fun getAllGrades(): Resource<List<GradeResponse>>
    suspend fun getGradeById(id: Long): Resource<GradeDetailResponse>
    suspend fun updateGrade(id: Long, request: GradeRequest): Resource<GradeResponse>
    suspend fun deleteGrade(id: Long): Resource<Unit>
}