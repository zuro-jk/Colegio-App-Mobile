package com.jeyix.schooljeyix.domain.usecase.enrollment

import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentResponse
import com.jeyix.schooljeyix.domain.repository.EnrollmentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class GetAllEnrollmentsUseCase @Inject constructor(
    private val repository: EnrollmentRepository
) {
    suspend operator fun invoke(): Resource<List<EnrollmentResponse>> {
        return repository.getAllEnrollments()
    }
}