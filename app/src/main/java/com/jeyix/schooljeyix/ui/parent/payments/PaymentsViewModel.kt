package com.jeyix.schooljeyix.ui.parent.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.remote.feature.payment.response.StripePaymentResponse
import com.jeyix.schooljeyix.domain.model.PaymentItem
import com.jeyix.schooljeyix.domain.usecase.enrollment.GetMyEnrollmentsUseCase
import com.jeyix.schooljeyix.domain.usecase.payment.ConfirmBulkPaymentUseCase
import com.jeyix.schooljeyix.domain.usecase.payment.ConfirmPaymentUseCase
import com.jeyix.schooljeyix.domain.usecase.payment.InitiateBulkPaymentUseCase
import com.jeyix.schooljeyix.domain.usecase.payment.InitiatePaymentUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val getMyEnrollmentsUseCase: GetMyEnrollmentsUseCase,
    private val initiatePaymentUseCase: InitiatePaymentUseCase,
    private val confirmPaymentUseCase: ConfirmPaymentUseCase,
    private val initiateBulkPaymentUseCase: InitiateBulkPaymentUseCase,
    private val confirmBulkPaymentUseCase: ConfirmBulkPaymentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentsUiState())
    val uiState = _uiState.asStateFlow()

    private val _paymentEvent = Channel<PaymentEvent>()
    val paymentEvent = _paymentEvent.receiveAsFlow()

    var isProcessingBulkPayment = false
    var currentEnrollmentId: Long? = null
    private val DISCOUNT_PERCENTAGE = BigDecimal("0.10")

    init {
        loadPayments()
    }

    fun loadPayments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getMyEnrollmentsUseCase()) {
                is Resource.Success -> {
                    val enrollments = result.data!!

                    val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale("es", "ES"))


                    val allPayments = enrollments.flatMap { enrollment ->
                        enrollment.payments.map { payment ->
                            val dueDate = LocalDate.parse(payment.dueDate)

                            val monthName = dueDate.format(monthFormatter)
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es", "ES")) else it.toString() }

                            PaymentItem(
                                id = payment.id,
                                enrollmentId = enrollment.id,
                                amount = payment.amount,
                                dueDate = dueDate,
                                isPaid = payment.isPaid,
                                isOverdue = !payment.isPaid && dueDate.isBefore(LocalDate.now()),
                                concept = "Cuota de $monthName",
                                studentName = enrollment.student.fullName
                            )
                        }
                    }.sortedBy { it.dueDate }

                    val totalDue = allPayments.filter { !it.isPaid }.sumOf { it.amount }

                    val discountAmount = totalDue.multiply(DISCOUNT_PERCENTAGE)
                    val discountedTotal = totalDue.subtract(discountAmount)

                    val canApplyDiscount = LocalDate.now().isBefore(LocalDate.of(2025, 12, 31))

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            paymentItems = allPayments,
                            totalDue = totalDue,
                            discountedTotal = discountedTotal,
                            discountAmount = discountAmount,
                            canApplyDiscount = canApplyDiscount
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun onPayClicked(paymentId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = initiatePaymentUseCase(paymentId)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    result.data?.let { response ->
                        _paymentEvent.send(PaymentEvent.LaunchStripeSheet(response))
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _paymentEvent.send(PaymentEvent.ShowError(result.message ?: "Error al iniciar pago"))
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun confirmPayment(paymentId: Long, paymentIntentId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = confirmPaymentUseCase(paymentId, paymentIntentId)

            when (result) {
                is Resource.Success -> {
                    loadPayments()
                    _paymentEvent.send(PaymentEvent.ShowMessage("¡Pago registrado correctamente!"))
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _paymentEvent.send(PaymentEvent.ShowError("Se cobró en Stripe pero falló la confirmación en el servidor: ${result.message}"))
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun onPayAllClicked(enrollmentId: Long) {
        isProcessingBulkPayment = true
        currentEnrollmentId = enrollmentId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = initiateBulkPaymentUseCase(enrollmentId)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    result.data?.let { response ->
                        _paymentEvent.send(PaymentEvent.LaunchStripeSheet(response))
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _paymentEvent.send(PaymentEvent.ShowError(result.message ?: "Error al iniciar pago total"))
                    isProcessingBulkPayment = false
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun toggleDiscount(active: Boolean) {
        _uiState.update { it.copy(isDiscountActive = active) }
    }

    fun confirmBulkPayment(paymentIntentId: String) {
        val enrollmentId = currentEnrollmentId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = confirmBulkPaymentUseCase(enrollmentId, paymentIntentId)

            when (result) {
                is Resource.Success -> {
                    loadPayments()
                    _paymentEvent.send(PaymentEvent.ShowMessage("¡Pago anual completado con éxito!"))
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _paymentEvent.send(PaymentEvent.ShowError("Error al confirmar pago masivo: ${result.message}"))
                }
                is Resource.Loading -> {}
            }
            // Limpiamos el estado
            isProcessingBulkPayment = false
            currentEnrollmentId = null
        }
    }

    sealed class PaymentEvent {
        data class LaunchStripeSheet(val stripeData: StripePaymentResponse) : PaymentEvent()
        data class ShowError(val message: String) : PaymentEvent()
        data class ShowMessage(val message: String) : PaymentEvent()
    }
}