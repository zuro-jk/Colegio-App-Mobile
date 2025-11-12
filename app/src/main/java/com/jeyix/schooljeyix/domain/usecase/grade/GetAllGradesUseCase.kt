package com.jeyix.schooljeyix.domain.usecase.grade

import com.jeyix.schooljeyix.data.remote.feature.grade.response.GradeResponse
import com.jeyix.schooljeyix.domain.repository.GradeRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class GetAllGradesUseCase @Inject constructor(
    private val repository: GradeRepository
) {
    suspend operator fun invoke(): Resource<List<GradeResponse>> {
        return repository.getAllGrades()
    }
}