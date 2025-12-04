package com.jeyix.schooljeyix.data.repository.payment

import com.jeyix.schooljeyix.data.remote.feature.payment.api.PaymentApi
import com.jeyix.schooljeyix.data.remote.feature.payment.request.BulkPaymentRequest
import com.jeyix.schooljeyix.data.remote.feature.payment.request.ConfirmBulkPaymentRequest
import com.jeyix.schooljeyix.data.remote.feature.payment.request.ConfirmPaymentRequest
import com.jeyix.schooljeyix.data.remote.feature.payment.request.PaymentInitiationRequest
import com.jeyix.schooljeyix.data.remote.feature.payment.response.StripePaymentResponse
import com.jeyix.schooljeyix.domain.repository.PaymentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApi
) : PaymentRepository {

    override suspend fun initiatePayment(paymentId: Long): Resource<StripePaymentResponse> {
        return try {
            val request = PaymentInitiationRequest(paymentId)
            val response = api.initiatePayment(request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al iniciar pago")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión")
        }
    }

    override suspend fun confirmPayment(paymentId: Long, paymentIntentId: String): Resource<Unit> {
        return try {
            val request = ConfirmPaymentRequest(paymentIntentId)
            val response = api.confirmPayment(paymentId, request)

            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al confirmar el pago")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión")
        }
    }

    override suspend fun initiateBulkPayment(enrollmentId: Long): Resource<StripePaymentResponse> {
        return try {
            val request = BulkPaymentRequest(enrollmentId)
            val response = api.initiateBulkPayment(request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al iniciar pago masivo")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión")
        }
    }

    override suspend fun confirmBulkPayment(enrollmentId: Long, paymentIntentId: String): Resource<Unit> {
        return try {
            val request = ConfirmBulkPaymentRequest(enrollmentId, paymentIntentId)
            val response = api.confirmBulkPayment(request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al confirmar pago total")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión")
        }
    }
}