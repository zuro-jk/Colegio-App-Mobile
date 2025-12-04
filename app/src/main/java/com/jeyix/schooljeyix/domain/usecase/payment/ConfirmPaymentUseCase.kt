package com.jeyix.schooljeyix.domain.usecase.payment

import com.jeyix.schooljeyix.domain.repository.PaymentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class ConfirmPaymentUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke(paymentId: Long, paymentIntentId: String): Resource<Unit> {
        return repository.confirmPayment(paymentId, paymentIntentId)
    }
}