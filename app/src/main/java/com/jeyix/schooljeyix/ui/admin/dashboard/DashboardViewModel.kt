package com.jeyix.schooljeyix.ui.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentStatus
import com.jeyix.schooljeyix.domain.repository.StudentRepository
import com.jeyix.schooljeyix.domain.usecase.enrollment.GetAllEnrollmentsUseCase
import com.jeyix.schooljeyix.domain.usecase.notifications.NotificationUseCases
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val notificationUseCases: NotificationUseCases,
    private val getEnrollmentsUseCase: GetAllEnrollmentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val studentsDeferred = async { studentRepository.getAllStudents() }
            val notificationsDeferred = async { notificationUseCases.getAnnouncements() }
            val enrollmentsDeferred = async { getEnrollmentsUseCase() }

            val studentsResult = studentsDeferred.await()
            val notificationsResult = notificationsDeferred.await()
            val enrollmentsResult = enrollmentsDeferred.await()

            val studentCount = if (studentsResult is Resource.Success) {
                studentsResult.data?.size ?: 0
            } else 0

            val alertsCount = if (notificationsResult is Resource.Success) {
                notificationsResult.data?.count {
                    it.type.name == "URGENT" || it.type.name == "PAYMENT"
                } ?: 0
            } else 0

            var totalRealIncome = 0.0
            val activityList = mutableListOf<ActivityItem>()
            val chartValues = FloatArray(5) { 0f }

            if (enrollmentsResult is Resource.Success) {
                val enrollments = enrollmentsResult.data ?: emptyList()

                activityList.addAll(enrollments.take(5).map { enrollment ->
                    ActivityItem(
                        title = if(enrollment.status == EnrollmentStatus.PAID) "Matrícula Completada" else "Matrícula Pendiente",
                        subtitle = "Estudiante: ${enrollment.student.fullName}",
                        timestamp = try {
                            LocalDate.parse(enrollment.academicYear.take(10)).format(
                                DateTimeFormatter.ofPattern("dd MMM"))
                        } catch (e: Exception) { enrollment.academicYear },
                        type = if(enrollment.status == EnrollmentStatus.PAID) ActivityType.PAYMENT else ActivityType.NEW_STUDENT
                    )
                })

                val today = LocalDate.now()
                val weekFields = WeekFields.of(Locale.getDefault())
                val weekOfYearNow = today.get(weekFields.weekOfWeekBasedYear())

                enrollments.forEach { enrollment ->
                    enrollment.payments.forEach { payment ->

                        if (payment.isPaid) {
                            val originalAmount = payment.amount.toDouble()
                            val discount = payment.appliedDiscount?.toDouble() ?: 0.0

                            val realPaidAmount = originalAmount - discount

                            totalRealIncome += realPaidAmount

                            val dateString = payment.paymentDate

                            if (dateString != null) {
                                try {
                                    val paidDate = LocalDate.parse(dateString.take(10))
                                    val weekOfPayment = paidDate.get(weekFields.weekOfWeekBasedYear())

                                    if (weekOfPayment == weekOfYearNow && paidDate.year == today.year) {
                                        val dayIndex = paidDate.dayOfWeek.value - 1

                                        if (dayIndex in 0..4) {
                                            chartValues[dayIndex] += realPaidAmount.toFloat()
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
            }

            // 5. Actualizar Estado
            _uiState.update {
                it.copy(
                    isLoading = false,
                    studentCount = studentCount,
                    pendingAlerts = alertsCount,
                    totalIncome = totalRealIncome,
                    recentActivity = activityList,
                    chartData = chartValues.toList()
                )
            }
        }
    }
}