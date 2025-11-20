package com.jeyix.schooljeyix.data.remote.feature.enrollment.response

import java.math.BigDecimal

data class FinanceDashboardResponse(
    val totalEnrollments: Long,
    val totalCollected: BigDecimal,
    val totalPending: BigDecimal,
    val totalOverdue: BigDecimal,
    val newStudentsThisMonth: Long
)
