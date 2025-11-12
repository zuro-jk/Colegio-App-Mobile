package com.jeyix.schooljeyix.domain.usecase.dashboard

import android.util.Log
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentResponse
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.PaymentSummary as ApiPaymentSummary
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.StudentSummary as ApiStudentSummary
import com.jeyix.schooljeyix.domain.model.PaymentSummary
import com.jeyix.schooljeyix.domain.usecase.enrollment.GetMyEnrollmentsUseCase
import com.jeyix.schooljeyix.domain.usecase.student.GetMyStudentsUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate

class GetDashboardDataUseCase @Inject constructor(
    private val getMyEnrollmentsUseCase: GetMyEnrollmentsUseCase,
    private val getMyStudentsUseCase: GetMyStudentsUseCase
) {

    suspend operator fun invoke(): Resource<DashboardRemoteData> {
        return try {
            coroutineScope {
                val studentsDeferred = async { getMyStudentsUseCase() }
                val enrollmentsDeferred = async { getMyEnrollmentsUseCase() }

                val studentsResult = studentsDeferred.await()
                val enrollmentsResult = enrollmentsDeferred.await()

                if (studentsResult is Resource.Error) {
                    return@coroutineScope Resource.Error(studentsResult.message ?: "Error al cargar estudiantes.")
                }
                if (enrollmentsResult is Resource.Error) {
                    return@coroutineScope Resource.Error(enrollmentsResult.message ?: "Error al cargar matrículas.")
                }

                val studentsFromApi = (studentsResult as Resource.Success).data!!
                val enrollments = (enrollmentsResult as Resource.Success).data!!

                val processedStudents = studentsFromApi.map { student ->
                    val avatarUrl = if (!student.user.profileImageUrl.isNullOrBlank()) {
                        student.user.profileImageUrl
                    } else {
                        "https://api.dicebear.com/8.x/adventurer/svg?seed=${student.user.username}"
                    }
                    ApiStudentSummary(
                        id = student.id,
                        fullName = student.user.fullName,
                        gradeLevel = student.gradeLevel,
                        profileImageUrl = avatarUrl
                    )
                }

                val remoteData = DashboardRemoteData(
                    nextPayment = findNextPaymentFrom(enrollments),
                    overduePaymentsCount = countOverduePaymentsIn(enrollments),
                    students = processedStudents
                )
                Resource.Success(remoteData)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Ocurrió un error inesperado")
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
                    profileImageUrl = apiStudent.profileImageUrl
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