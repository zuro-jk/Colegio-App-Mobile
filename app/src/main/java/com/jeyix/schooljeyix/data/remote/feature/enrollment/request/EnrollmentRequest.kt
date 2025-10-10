package com.jeyix.schooljeyix.data.remote.feature.enrollment.request

import java.math.BigDecimal

data class EnrollmentRequest(
    val studentId: Long,
    val academicYear: String,
    val totalAmount: BigDecimal,
    val numberOfInstallments: Int
)
