package com.jeyix.schooljeyix.domain.usecase.dashboard

import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.PaymentSummary
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.StudentSummary

data class DashboardRemoteData (
    val nextPayment: PaymentSummary?,
    val overduePaymentsCount: Int,
    val students: List<StudentSummary>
)