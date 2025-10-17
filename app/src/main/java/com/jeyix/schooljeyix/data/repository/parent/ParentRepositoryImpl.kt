package com.jeyix.schooljeyix.data.repository.parent

import com.google.gson.JsonParseException
import com.jeyix.schooljeyix.data.remote.feature.parent.api.ParentApi
import com.jeyix.schooljeyix.data.remote.feature.parent.request.ParentRequest
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentDetailResponse
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.domain.usecase.parent.ParentRepository
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class ParentRepositoryImpl @Inject constructor(
    private val api: ParentApi
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

    override suspend fun addParent(parentRequest: ParentRequest): Resource<ParentResponse> {
        return try {
            val response = api.addParent(parentRequest)
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

    override suspend fun updateParentById(
        id: Long,
        parentRequest: ParentRequest
    ): Resource<ParentResponse> {
        return try {
            val response = api.updateParentById(id, parentRequest)
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

    override suspend fun deleteParentById(id: Long): Resource<Unit> {
        return try {
            val response = api.deleteParentById(id)
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