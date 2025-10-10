package com.jeyix.schooljeyix.domain.usecase.enrollment

import com.jeyix.schooljeyix.data.remote.feature.enrollment.request.EnrollmentRequest
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentResponse
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject
import java.math.BigDecimal

class CreateEnrollmentUseCase @Inject constructor(
    private val repository: EnrollmentRepository
) {

    suspend operator fun invoke(
        enrollmentRequest: EnrollmentRequest
    ): Resource<EnrollmentResponse> {

        if (enrollmentRequest.academicYear.isBlank()) {
            return Resource.Error("El año académico no puede estar vacío.")
        }
        if (enrollmentRequest.totalAmount <= BigDecimal.ZERO) {
            return Resource.Error("El monto total debe ser mayor a cero.")
        }
        if (enrollmentRequest.numberOfInstallments < 1) {
            return Resource.Error("Debe haber al menos una cuota.")
        }

        return repository.createEnrollment(enrollmentRequest)
    }

}