package com.jeyix.schooljeyix.domain.repository

import com.jeyix.schooljeyix.data.remote.feature.payment.response.StripePaymentResponse
import com.jeyix.schooljeyix.domain.util.Resource

interface PaymentRepository {
    suspend fun initiatePayment(paymentId: Long): Resource<StripePaymentResponse>
    suspend fun confirmPayment(paymentId: Long, paymentIntentId: String): Resource<Unit>
    suspend fun initiateBulkPayment(enrollmentId: Long): Resource<StripePaymentResponse>
    suspend fun confirmBulkPayment(enrollmentId: Long, paymentIntentId: String): Resource<Unit>
}