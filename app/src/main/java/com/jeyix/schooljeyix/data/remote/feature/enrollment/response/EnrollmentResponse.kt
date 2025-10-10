package com.jeyix.schooljeyix.data.remote.feature.enrollment.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class EnrollmentResponse(
    val id: Long,
    val student: StudentSummary,
    val academicYear: String,
    val totalAmount: BigDecimal,
    val status: EnrollmentStatus,
    val payments: List<PaymentSummary>
)

enum class EnrollmentStatus {
    @SerializedName("PENDING_PAYMENT")
    PENDING_PAYMENT,
    @SerializedName("PAID")
    PAID,
    @SerializedName("CANCELLED")
    CANCELLED
}