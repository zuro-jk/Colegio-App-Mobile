package com.jeyix.schooljeyix.data.repository.student

import android.net.Uri
import com.jeyix.schooljeyix.data.remote.feature.student.api.StudentApi
import com.jeyix.schooljeyix.data.remote.feature.student.request.CreateStudentRequest
import com.jeyix.schooljeyix.data.remote.feature.student.request.UpdateStudentRequest
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.data.util.FileUtil
import com.jeyix.schooljeyix.domain.usecase.student.StudentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class StudentRepositoryImpl @Inject constructor(
    private val api: StudentApi,
    private val fileUtil: FileUtil
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

    override suspend fun createStudent(request: CreateStudentRequest): Resource<StudentResponse> {
        return try {
            val response = api.createStudent(request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al crear el estudiante.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun updateStudentProfileImage(
        studentId: Long,
        imageUri: Uri
    ): Resource<StudentResponse> {
        return try {
            // Usamos FileUtil para crear el MultipartBody.Part
            val multipartBody = fileUtil.createMultipartBody(imageUri, "file")
                ?: return Resource.Error("No se pudo procesar el archivo local.")

            // Llamamos a la API
            val response = api.uploadStudentProfileImage(studentId, multipartBody)

            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al subir la imagen.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun updateStudent(studentId: Long, request: UpdateStudentRequest): Resource<StudentResponse> {
        return try {
            val response = api.updateStudent(studentId, request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al actualizar el estudiante.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun deleteStudent(studentId: Long): Resource<Unit> {
        return try {
            val response = api.deleteStudent(studentId)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al eliminar el estudiante.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

}