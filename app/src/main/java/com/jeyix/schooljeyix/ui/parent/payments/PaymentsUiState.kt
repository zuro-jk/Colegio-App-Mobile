package com.jeyix.schooljeyix.ui.parent.payments

import com.jeyix.schooljeyix.domain.model.PaymentItem
import java.math.BigDecimal

data class PaymentsUiState(
    val isLoading: Boolean = true,
    val totalDueThisMonth: BigDecimal = BigDecimal.ZERO,
    val paymentItems: List<PaymentItem> = emptyList(),
    val error: String? = null
)