package com.jeyix.schooljeyix.data.remote.feature.enrollment.response

import java.math.BigDecimal

data class PaymentSummary(
    val id: Long,
    val paymentDate: String?,
    val amount: BigDecimal,
    val appliedDiscount: BigDecimal? = null,
    val dueDate: String,
    val isPaid: Boolean
)
