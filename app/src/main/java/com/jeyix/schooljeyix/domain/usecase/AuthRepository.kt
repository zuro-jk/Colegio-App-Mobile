package com.jeyix.schooljeyix.domain.usecase

import com.jeyix.schooljeyix.data.remote.core.ApiResponse
import com.jeyix.schooljeyix.data.remote.feature.auth.response.AuthResponse
import com.jeyix.schooljeyix.domain.model.User

interface AuthRepository {
    suspend fun login(usernameOrEmail: String, password: String): ApiResponse<AuthResponse>
    suspend fun register(user: User): ApiResponse<Int>
}