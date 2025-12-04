package com.jeyix.schooljeyix.data.remote.feature.payment.request

data class ConfirmBulkPaymentRequest(
    val enrollmentId: Long,
    val paymentIntentId: String
)