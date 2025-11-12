package com.jeyix.schooljeyix.data.repository.parent

import android.net.Uri
import com.google.gson.JsonParseException
import com.jeyix.schooljeyix.data.remote.feature.parent.api.ParentApi
import com.jeyix.schooljeyix.data.remote.feature.parent.request.CreateParentRequest
import com.jeyix.schooljeyix.data.remote.feature.parent.request.UpdateParentRequest
import com.jeyix.schooljeyix.data.remote.feature.parent.response.detail.ParentDetailResponse
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.data.util.FileUtil
import com.jeyix.schooljeyix.domain.usecase.parent.ParentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class ParentRepositoryImpl @Inject constructor(
    private val api: ParentApi,
    private val fileUtil: FileUtil
) : ParentRepository {

    override suspend fun getAllParents(): Resource<List<ParentResponse>> {
        return try {
            val response = api.getAllParents()
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(
                    response.errorBody()?.string() ?: "Error al obtener la lista de padres."
                )
            }
        } catch (e: JsonParseException) {
            Resource.Error("Error de formato en la respuesta del servidor. Contacte a soporte.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getParentById(id: Long): Resource<ParentResponse> {
        return try {
            val response = api.getParentById(id)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Padre no encontrado.")
            }
        } catch (e: JsonParseException) {
            Resource.Error("Error de formato en la respuesta del servidor. Contacte a soporte.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getParentByIdDetails(id: Long): Resource<ParentDetailResponse> {
        return try {
            val response = api.getParentByIdDetails(id)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Padre no encontrado.")
            }
        } catch (e: JsonParseException) {
            Resource.Error("Error de formato en la respuesta del servidor. Contacte a soporte.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun createParent(request: CreateParentRequest): Resource<ParentResponse> {
        return try {
            val response = api.createParent(request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al añadir el padre.")
            }
        } catch (e: JsonParseException) {
            Resource.Error("Error de formato en la respuesta del servidor. Contacte a soporte.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun updateParentProfileImage(
        parentId: Long,
        imageUri: Uri
    ): Resource<ParentResponse> {
        return try {
            val multipartBody = fileUtil.createMultipartBody(imageUri, "file")
                ?: return Resource.Error("No se pudo procesar el archivo local.")

            // Llamamos a la API
            val response = api.uploadParentProfileImage(parentId, multipartBody)

            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al subir la imagen.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    // Renombrado de 'updateParentById' a 'updateParent'
    // Renombrado de 'parentRequest' a 'request'
    override suspend fun updateParent(
        id: Long,
        request: UpdateParentRequest
    ): Resource<ParentResponse> {
        return try {
            // Asumiendo que tu ParentApi también fue renombrada
            val response = api.updateParent(id, request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al actualizar el padre.")
            }
        } catch (e: JsonParseException) {
            Resource.Error("Error de formato en la respuesta del servidor. Contacte a soporte.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    // Renombrado de 'deleteParentById' a 'deleteParent'
    override suspend fun deleteParent(id: Long): Resource<Unit> {
        return try {
            // Asumiendo que tu ParentApi también fue renombrada
            val response = api.deleteParent(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al eliminar el padre.")
            }
        } catch (e: JsonParseException) {
            Resource.Error("Error de formato en la respuesta del servidor. Contacte a soporte.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }
}