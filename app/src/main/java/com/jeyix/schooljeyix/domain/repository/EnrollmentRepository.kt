package com.jeyix.schooljeyix.domain.repository

import com.jeyix.schooljeyix.data.remote.feature.enrollment.request.EnrollmentRequest
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentResponse
import com.jeyix.schooljeyix.domain.util.Resource

interface EnrollmentRepository {

    /**
     * ADMIN: Obtiene todas las matrículas del sistema.
     */
    suspend fun getAllEnrollments(): Resource<List<EnrollmentResponse>>

    /**
     * PADRE: Obtiene la lista de matrículas de sus hijos.
     */
    suspend fun getMyEnrollments(): Resource<List<EnrollmentResponse>>

    /**
     * Obtiene los detalles de una matrícula específica por su ID.
     */
    suspend fun getEnrollmentById(id: Long): Resource<EnrollmentResponse>

    /**
     * Crea una nueva matrícula.
     */
    suspend fun createEnrollment(request: EnrollmentRequest): Resource<EnrollmentResponse>

}