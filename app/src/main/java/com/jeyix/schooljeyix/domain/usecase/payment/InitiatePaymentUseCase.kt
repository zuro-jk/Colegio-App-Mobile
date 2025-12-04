package com.jeyix.schooljeyix.domain.usecase.payment

import com.jeyix.schooljeyix.data.remote.feature.payment.response.StripePaymentResponse
import com.jeyix.schooljeyix.domain.repository.PaymentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class InitiatePaymentUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke(paymentId: Long): Resource<StripePaymentResponse> {
        return repository.initiatePayment(paymentId)
    }
}