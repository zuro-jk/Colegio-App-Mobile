package com.jeyix.schooljeyix.data.repository.enrollment

import com.jeyix.schooljeyix.data.remote.feature.enrollment.api.EnrollmentApi
import com.jeyix.schooljeyix.domain.usecase.enrollment.EnrollmentRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class EnrollmentRepositoryImpl @Inject constructor(
    private val api: EnrollmentApi
): EnrollmentRepository {


}