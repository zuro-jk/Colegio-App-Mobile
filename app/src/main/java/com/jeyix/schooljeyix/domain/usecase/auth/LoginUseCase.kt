package com.jeyix.schooljeyix.domain.usecase.auth

import com.jeyix.schooljeyix.data.remote.feature.auth.request.LoginRequest
import com.jeyix.schooljeyix.data.remote.feature.auth.response.AuthResponse
import com.jeyix.schooljeyix.domain.util.Resource
import jakarta.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(loginRequest: LoginRequest): Resource<AuthResponse> {
        if (loginRequest.usernameOrEmail.isBlank()) {
            return Resource.Error("El nombre de usuario o correo no puede estar vacío.")
        }
        if (loginRequest.password.length < 6) {
            return Resource.Error("La contraseña debe tener al menos 6 caracteres.")
        }

        val result = repository.login(loginRequest)

        if (result is Resource.Success) {
            // Quizás quieres guardar el token en algún lado o registrar un evento.
        }

        return result
    }
}