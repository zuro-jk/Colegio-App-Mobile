package com.jeyix.schooljeyix.ui.parent.dashboard

import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.PaymentSummary
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.StudentSummary

data class DashboardUiState(
    val isLoading: Boolean = true,
    val parentName: String = "",
    val parentAvatarUrl: String? = null,
    val nextPayment: PaymentSummary? = null,
    val overduePaymentsCount: Int = 0,
    val students: List<StudentSummary> = emptyList(),
    val error: String? = null
)