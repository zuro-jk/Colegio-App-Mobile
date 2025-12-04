package com.jeyix.schooljeyix.domain.usecase.payment

import com.jeyix.schooljeyix.domain.repository.PaymentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class ConfirmBulkPaymentUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke(enrollmentId: Long, paymentIntentId: String): Resource<Unit> {
        return repository.confirmBulkPayment(enrollmentId, paymentIntentId)
    }
}