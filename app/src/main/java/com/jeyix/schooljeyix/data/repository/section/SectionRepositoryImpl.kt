package com.jeyix.schooljeyix.data.repository.section

import com.jeyix.schooljeyix.data.remote.feature.section.api.SectionApi
import com.jeyix.schooljeyix.data.remote.feature.section.request.SectionRequest
import com.jeyix.schooljeyix.data.remote.feature.section.response.SectionResponse
import com.jeyix.schooljeyix.domain.repository.SectionRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SectionRepositoryImpl @Inject constructor(
    private val api: SectionApi
) : SectionRepository {

    override suspend fun createSection(request: SectionRequest): Resource<SectionResponse> {
        return try {
            val response = api.createSection(request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al crear la sección.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getAllSections(): Resource<List<SectionResponse>> {
        return try {
            val response = api.getAllSections()
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al obtener secciones.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getSectionsByGradeId(gradeId: Long): Resource<List<SectionResponse>> {
        return try {
            val response = api.getSectionsByGradeId(gradeId)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al obtener secciones por grado.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getSectionById(id: Long): Resource<SectionResponse> {
        return try {
            val response = api.getSectionById(id)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al obtener detalle de la sección.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun updateSection(id: Long, request: SectionRequest): Resource<SectionResponse> {
        return try {
            val response = api.updateSection(id, request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al actualizar la sección.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun deleteSection(id: Long): Resource<Unit> {
        return try {
            val response = api.deleteSection(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al eliminar la sección.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }
}