package com.jeyix.schooljeyix.data.repository.auth

import com.jeyix.schooljeyix.data.remote.core.ApiResponse
import com.jeyix.schooljeyix.data.remote.feature.auth.api.AuthApi
import com.jeyix.schooljeyix.data.remote.feature.auth.request.LoginRequest
import com.jeyix.schooljeyix.data.remote.feature.auth.request.RegisterRequest
import com.jeyix.schooljeyix.data.remote.feature.auth.response.AuthResponse
import com.jeyix.schooljeyix.domain.model.User
import com.jeyix.schooljeyix.domain.usecase.AuthRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun login(usernameOrEmail: String, password: String): ApiResponse<AuthResponse> {
        val response = api.login(LoginRequest(usernameOrEmail, password))
        return response.body() ?: ApiResponse(false, "Error en login", null)
    }

    override suspend fun register(user: User): ApiResponse<Int> {
        val request = RegisterRequest(
            firstName = user.firstName,
            lastName = user.lastName,
            username = user.username,
            email = user.email,
            password = user.password
        )
        val response = api.register(request)
        return response.body() ?: ApiResponse(false, "Error en registro", null)
    }
}