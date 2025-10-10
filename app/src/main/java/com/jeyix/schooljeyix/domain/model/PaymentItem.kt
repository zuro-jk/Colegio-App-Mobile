package com.jeyix.schooljeyix.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class PaymentItem(
    val id: Long,
    val amount: BigDecimal,
    val dueDate: LocalDate,
    val isPaid: Boolean,
    val isOverdue: Boolean,
    val concept: String,
    val studentName: String
)