package com.jeyix.schooljeyix.domain.repository

import com.jeyix.schooljeyix.data.remote.feature.auth.request.LoginRequest
import com.jeyix.schooljeyix.data.remote.feature.auth.request.RefreshRequest
import com.jeyix.schooljeyix.data.remote.feature.auth.request.RegisterRequest
import com.jeyix.schooljeyix.data.remote.feature.auth.request.SessionLogoutRequest
import com.jeyix.schooljeyix.data.remote.feature.auth.response.AuthResponse
import com.jeyix.schooljeyix.domain.util.Resource

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): Resource<AuthResponse>
    suspend fun register(registerRequest: RegisterRequest): Resource<Int>

    suspend fun refreshToken(refreshRequest: RefreshRequest): Resource<AuthResponse>

    suspend fun logout(sessionLogoutRequest: SessionLogoutRequest): Resource<Unit>
}