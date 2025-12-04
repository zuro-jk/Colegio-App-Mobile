package com.jeyix.schooljeyix.data.remote.feature.payment.api

import com.jeyix.schooljeyix.data.remote.core.ApiResponse
import com.jeyix.schooljeyix.data.remote.feature.payment.request.BulkPaymentRequest
import com.jeyix.schooljeyix.data.remote.feature.payment.request.ConfirmBulkPaymentRequest
import com.jeyix.schooljeyix.data.remote.feature.payment.request.ConfirmPaymentRequest
import com.jeyix.schooljeyix.data.remote.feature.payment.request.PaymentInitiationRequest
import com.jeyix.schooljeyix.data.remote.feature.payment.response.StripePaymentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path


interface PaymentApi {

    @POST("payments/initiate")
    suspend fun initiatePayment(
        @Body request: PaymentInitiationRequest
    ): Response<ApiResponse<StripePaymentResponse>>

    @POST("payments/{id}/confirm")
    suspend fun confirmPayment(
        @Path("id") paymentId: Long,
        @Body request: ConfirmPaymentRequest
    ): Response<ApiResponse<Unit>>

    @POST("payments/initiate-bulk")
    suspend fun initiateBulkPayment(
        @Body request: BulkPaymentRequest
    ): Response<ApiResponse<StripePaymentResponse>>

    @POST("payments/confirm-bulk")
    suspend fun confirmBulkPayment(
        @Body request: ConfirmBulkPaymentRequest
    ): Response<ApiResponse<Unit>>

}