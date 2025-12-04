package com.jeyix.schooljeyix.ui.admin.enrollments.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.remote.feature.enrollment.request.EnrollmentRequest
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.domain.usecase.enrollment.CreateEnrollmentUseCase
import com.jeyix.schooljeyix.domain.usecase.student.GetAllStudentsUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CreateEnrollmentViewModel @Inject constructor(
    private val createEnrollmentUseCase: CreateEnrollmentUseCase,
    private val getAllStudentsUseCase: GetAllStudentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading(null))
    val uiState = _uiState.asStateFlow()

    private val _studentsList = MutableStateFlow<List<StudentResponse>>(emptyList())
    val studentsList = _studentsList.asStateFlow()

    var selectedStudentId: Long? = null

    init {
        loadStudentsForSelector()
    }

    private fun loadStudentsForSelector() {
        viewModelScope.launch {
            when (val result = getAllStudentsUseCase()) {
                is Resource.Success -> {
                    _studentsList.value = result.data ?: emptyList()
                    _uiState.value = Resource.Loading(null)
                }
                is Resource.Error -> {
                    _uiState.value = Resource.Error("Error al cargar lista de estudiantes: ${result.message}")
                }
                else -> {}
            }
        }
    }

    fun createEnrollment(academicYear: String, amountStr: String, installmentsStr: String) {
        viewModelScope.launch {
            _uiState.value = Resource.Loading()

            if (selectedStudentId == null || selectedStudentId == 0L) {
                _uiState.value = Resource.Error("Debes seleccionar un estudiante de la lista.")
                return@launch
            }
            if (academicYear.isBlank() || amountStr.isBlank() || installmentsStr.isBlank()) {
                _uiState.value = Resource.Error("Todos los campos son obligatorios.")
                return@launch
            }

            val request = EnrollmentRequest(
                studentId = selectedStudentId!!,
                academicYear = academicYear,
                totalAmount = amountStr.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                numberOfInstallments = installmentsStr.toIntOrNull() ?: 0
            )

            val result = createEnrollmentUseCase(request)

            if (result is Resource.Success) {
                _uiState.value = Resource.Success(Unit)
            } else if (result is Resource.Error) {
                _uiState.value = Resource.Error(result.message ?: "Error desconocido")
            }
        }
    }
}