package com.jeyix.schooljeyix.domain.usecase.enrollment

import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentResponse
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject

class GetMyEnrollmentsUseCase @Inject constructor(
    private val repository: EnrollmentRepository
) {
    suspend operator fun invoke(): Resource<List<EnrollmentResponse>> {
        return repository.getMyEnrollments()
    }
}