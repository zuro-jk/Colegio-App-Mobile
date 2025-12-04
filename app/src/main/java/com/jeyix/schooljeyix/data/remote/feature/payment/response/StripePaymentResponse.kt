package com.jeyix.schooljeyix.data.remote.feature.payment.response

data class StripePaymentResponse(
    val paymentIntentClientSecret: String,
    val publishableKey: String,
    val ephemeralKey: String? = null,
    val customerId: String? = null
)
