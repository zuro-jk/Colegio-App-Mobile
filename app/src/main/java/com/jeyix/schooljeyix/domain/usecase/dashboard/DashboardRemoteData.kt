package com.jeyix.schooljeyix.domain.usecase.dashboard

import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.StudentSummary
import com.jeyix.schooljeyix.domain.model.PaymentSummary

data class DashboardRemoteData (
    val nextPayment: PaymentSummary?,
    val overduePaymentsCount: Int,
    val students: List<StudentSummary>
)