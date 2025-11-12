package com.jeyix.schooljeyix.domain.repository

import com.jeyix.schooljeyix.data.remote.feature.enrollment.request.EnrollmentRequest
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentResponse
import com.jeyix.schooljeyix.domain.util.Resource

interface EnrollmentRepository {


    /**
     * Obtiene la lista de matrículas de los hijos del padre autenticado.
     */
    suspend fun getMyEnrollments(): Resource<List<EnrollmentResponse>>

    /**
     * Obtiene los detalles de una matrícula específica por su ID.
     */
    suspend fun getEnrollmentById(id: Long): Resource<EnrollmentResponse>

    /**
     * Crea una nueva matrícula para un estudiante.
     */
    suspend fun createEnrollment(request: EnrollmentRequest): Resource<EnrollmentResponse>

}