package com.jeyix.schooljeyix.domain.model

import java.math.BigDecimal

data class PaymentSummary (
    val amount: BigDecimal,
    val dueDate: String,
    val studentName: String
)