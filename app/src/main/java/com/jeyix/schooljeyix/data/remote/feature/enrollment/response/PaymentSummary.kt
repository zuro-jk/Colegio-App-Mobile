package com.jeyix.schooljeyix.data.remote.feature.enrollment.response

import java.math.BigDecimal

data class PaymentSummary(
    val id: Long,
    val amount: BigDecimal,
    val dueDate: String,
    val isPaid: Boolean
)
