package com.jeyix.schooljeyix.data.repository.student

import com.jeyix.schooljeyix.data.remote.feature.student.api.StudentApi
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.domain.usecase.student.StudentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class StudentRepositoryImpl @Inject constructor(
    private val api: StudentApi
): StudentRepository {

    override suspend fun getAllStudents(): Resource<List<StudentResponse>> {
        return try {
            val response = api.getAllStudents()
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al obtener todos los estudiantes.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getStudentById(studentId: Long): Resource<StudentResponse> {
        return try {
            val response = api.getStudentById(studentId)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Estudiante no encontrado.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getStudentByParentId(parentId: Long): Resource<List<StudentResponse>> {
        return try {
            val response = api.getStudentByParentId(parentId)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al obtener estudiantes por padre.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getMyStudents(): Resource<List<StudentResponse>> {
        return try {
            val response = api.getMyStudents()
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al obtener tus estudiantes.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

}