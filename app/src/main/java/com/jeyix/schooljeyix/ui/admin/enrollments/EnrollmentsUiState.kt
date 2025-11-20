package com.jeyix.schooljeyix.ui.admin.enrollments

import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentResponse

data class EnrollmentsUiState(
    val isLoading: Boolean = true,
    val enrollments: List<EnrollmentResponse> = emptyList(),
    val error: String? = null
)