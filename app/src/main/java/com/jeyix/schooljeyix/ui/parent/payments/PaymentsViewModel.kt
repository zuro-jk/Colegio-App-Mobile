package com.jeyix.schooljeyix.ui.parent.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.domain.model.PaymentItem
import com.jeyix.schooljeyix.domain.usecase.enrollment.GetMyEnrollmentsUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val getMyEnrollmentsUseCase: GetMyEnrollmentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPayments()
    }

    fun loadPayments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getMyEnrollmentsUseCase()) {
                is Resource.Success -> {
                    val enrollments = result.data!!

                    // --- CORRECCIÓN 2: Formateador para el nombre del mes ---
                    val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale("es", "ES"))

                    val allPayments = enrollments.flatMap { enrollment ->
                        enrollment.payments.map { payment ->
                            val dueDate = LocalDate.parse(payment.dueDate)

                            val monthName = dueDate.format(monthFormatter)
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es", "ES")) else it.toString() }

                            PaymentItem(
                                id = payment.id,
                                amount = payment.amount,
                                dueDate = dueDate,
                                isPaid = payment.isPaid,
                                isOverdue = !payment.isPaid && dueDate.isBefore(LocalDate.now()),
                                concept = "Cuota de $monthName",
                                studentName = enrollment.student.fullName
                            )
                        }
                    }.sortedBy { it.dueDate }

                    val totalDue = allPayments
                        .filter { !it.isPaid }
                        .sumOf { it.amount }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            paymentItems = allPayments,
                            totalDueThisMonth = totalDue
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
}