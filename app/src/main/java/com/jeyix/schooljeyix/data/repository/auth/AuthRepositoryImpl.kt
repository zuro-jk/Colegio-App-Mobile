package com.jeyix.schooljeyix.data.repository.auth

import com.jeyix.schooljeyix.data.remote.feature.auth.api.AuthApi
import com.jeyix.schooljeyix.data.remote.feature.auth.request.LoginRequest
import com.jeyix.schooljeyix.data.remote.feature.auth.request.RefreshRequest
import com.jeyix.schooljeyix.data.remote.feature.auth.request.RegisterRequest
import com.jeyix.schooljeyix.data.remote.feature.auth.request.SessionLogoutRequest
import com.jeyix.schooljeyix.data.remote.feature.auth.response.AuthResponse
import com.jeyix.schooljeyix.domain.repository.AuthRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun login(loginRequest: LoginRequest): Resource<AuthResponse> {
        try {
            val response = api.login(loginRequest)

            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.data != null) {
                    return Resource.Success(apiResponse.data)
                } else {
                    return Resource.Error(apiResponse?.message ?: "Respuesta sin datos.")
                }
            } else {
                return Resource.Error(response.message() ?: "Error del servidor.")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Error de conexión. Verifica tu internet.")
        }
    }

    override suspend fun register(registerRequest: RegisterRequest): Resource<Int> {
        try {
            val response = api.register(registerRequest)
            if (response.isSuccessful && response.body()?.data != null) {
                return Resource.Success(response.body()!!.data!!)
            } else {
                return Resource.Error(response.errorBody()?.string() ?: "Error en el registro.")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun refreshToken(refreshRequest: RefreshRequest): Resource<AuthResponse> {
        try {
            val response = api.refreshToken(refreshRequest)
            if (response.isSuccessful && response.body()?.data != null) {
                return Resource.Success(response.body()!!.data!!)
            } else {
                return Resource.Error(response.errorBody()?.string() ?: "Error en el refresh token.")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun logout(sessionLogoutRequest: SessionLogoutRequest): Resource<Unit> {
        try {
            val response = api.logout(sessionLogoutRequest)
            if (response.isSuccessful && response.body()?.data != null) {
                return Resource.Success(response.body()!!.data!!)
            } else {
                return Resource.Error(response.errorBody()?.string() ?: "Error al cerrar sesión.")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Error de conexión.")
        }
    }
}