package com.jeyix.schooljeyix.domain.usecase.dashboard

import android.util.Log
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentResponse
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.PaymentSummary as ApiPaymentSummary
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.StudentSummary as ApiStudentSummary
import com.jeyix.schooljeyix.domain.model.PaymentSummary
import com.jeyix.schooljeyix.domain.usecase.enrollment.GetMyEnrollmentsUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject
import java.time.LocalDate

class GetDashboardDataUseCase @Inject constructor(
    private val getMyEnrollmentsUseCase: GetMyEnrollmentsUseCase
) {

    suspend operator fun invoke(): Resource<DashboardRemoteData> {
        val enrollmentsResult = getMyEnrollmentsUseCase()

        Log.d("DashboardDebug", "Resultado de GetMyEnrollmentsUseCase: $enrollmentsResult")

        return when (enrollmentsResult) {
            is Resource.Success -> {
                val enrollments = enrollmentsResult.data!!

                val remoteData = DashboardRemoteData(
                    nextPayment = findNextPaymentFrom(enrollments),
                    overduePaymentsCount = countOverduePaymentsIn(enrollments),
                    students = extractStudentsFrom(enrollments)
                )
                Resource.Success(remoteData)
            }
            is Resource.Error -> {
                Resource.Error(enrollmentsResult.message ?: "Ocurrió un error desconocido")
            }
            is Resource.Loading -> Resource.Loading()
        }
    }

    /**
     * Extrae una lista única de estudiantes a partir de las matrículas.
     */
    private fun extractStudentsFrom(enrollments: List<EnrollmentResponse>): List<ApiStudentSummary> {
        return enrollments
            .map { it.student }
            .distinctBy { it.id }
            .map { apiStudent ->
                ApiStudentSummary(
                    id = apiStudent.id,
                    fullName = apiStudent.fullName,
                    gradeLevel = apiStudent.gradeLevel,
                )
            }
    }

    /**
     * Cuenta cuántos pagos no están pagados y su fecha de vencimiento ya pasó.
     */
    private fun countOverduePaymentsIn(enrollments: List<EnrollmentResponse>): Int {
        val today = LocalDate.now()
        return enrollments.flatMap { it.payments }.count { payment ->
            !payment.isPaid && LocalDate.parse(payment.dueDate).isBefore(today)
        }
    }

    /**
     * Busca entre todos los pagos de todas las matrículas el próximo que no esté pagado.
     */
    private fun findNextPaymentFrom(enrollments: List<EnrollmentResponse>): PaymentSummary? {
        val today = LocalDate.now()

        var closestPair: Pair<ApiPaymentSummary, ApiStudentSummary>? = null

        for (enrollment in enrollments) {
            val closestPaymentInEnrollment = enrollment.payments
                .filter { !it.isPaid && !LocalDate.parse(it.dueDate).isBefore(today) }
                .minByOrNull { LocalDate.parse(it.dueDate) }

            if (closestPaymentInEnrollment != null) {
                if (closestPair == null ||
                    LocalDate.parse(closestPaymentInEnrollment.dueDate)
                        .isBefore(LocalDate.parse(closestPair!!.first.dueDate))) {
                    closestPair = Pair(closestPaymentInEnrollment, enrollment.student)
                }
            }
        }

        return closestPair?.let { (payment, student) ->
            PaymentSummary(
                amount = payment.amount,
                dueDate = payment.dueDate,
                studentName = student.fullName
            )
        }
    }
}