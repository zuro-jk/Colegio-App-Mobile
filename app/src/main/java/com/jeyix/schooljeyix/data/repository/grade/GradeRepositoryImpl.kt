package com.jeyix.schooljeyix.data.repository.grade

import com.jeyix.schooljeyix.data.remote.feature.grade.api.GradeApi
import com.jeyix.schooljeyix.data.remote.feature.grade.request.GradeRequest
import com.jeyix.schooljeyix.data.remote.feature.grade.response.GradeDetailResponse
import com.jeyix.schooljeyix.data.remote.feature.grade.response.GradeResponse
import com.jeyix.schooljeyix.domain.repository.GradeRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GradeRepositoryImpl @Inject constructor(
    private val api: GradeApi
) : GradeRepository {

    override suspend fun createGrade(request: GradeRequest): Resource<GradeResponse> {
        return try {
            val response = api.createGrade(request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al crear el grado.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getAllGrades(): Resource<List<GradeResponse>> {
        return try {
            val response = api.getAllGrades()
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al obtener grados.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getGradeById(id: Long): Resource<GradeDetailResponse> {
        return try {
            val response = api.getGradeById(id)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al obtener detalle del grado.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun updateGrade(id: Long, request: GradeRequest): Resource<GradeResponse> {
        return try {
            val response = api.updateGrade(id, request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al actualizar el grado.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun deleteGrade(id: Long): Resource<Unit> {
        return try {
            val response = api.deleteGrade(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al eliminar el grado.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }
}