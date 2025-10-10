package com.jeyix.schooljeyix.data.repository.enrollment

import com.jeyix.schooljeyix.data.remote.feature.enrollment.api.EnrollmentApi
import com.jeyix.schooljeyix.data.remote.feature.enrollment.request.EnrollmentRequest
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentResponse
import com.jeyix.schooljeyix.domain.usecase.enrollment.EnrollmentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class EnrollmentRepositoryImpl @Inject constructor(
    private val api: EnrollmentApi
): EnrollmentRepository {

    override suspend fun getMyEnrollments(): Resource<List<EnrollmentResponse>> {
        return try {
            val response = api.getMyEnrollments()
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al obtener matrículas.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getEnrollmentById(id: Long): Resource<EnrollmentResponse> {
        return try {
            val response = api.getEnrollmentById(id)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Matrícula no encontrada.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun createEnrollment(request: EnrollmentRequest): Resource<EnrollmentResponse> {
        return try {
            val response = api.createEnrollment(request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al crear la matrícula.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }


}