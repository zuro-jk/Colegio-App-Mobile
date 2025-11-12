package com.jeyix.schooljeyix.domain.usecase.auth

import android.util.Patterns
import com.jeyix.schooljeyix.data.remote.feature.auth.request.RegisterRequest
import com.jeyix.schooljeyix.domain.repository.AuthRepository
import com.jeyix.schooljeyix.domain.util.Resource
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        registerRequest: RegisterRequest,
        confirmPassword: String
    ): Resource<Int> {

        if (registerRequest.firstName.isBlank() || registerRequest.lastName.isBlank() || registerRequest.username.isBlank() || registerRequest.email.isBlank() || registerRequest.password.isBlank()) {
            return Resource.Error("Todos los campos son obligatorios.")
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(registerRequest.email).matches()) {
            return Resource.Error("El formato del correo electrónico no es válido.")
        }

        if (registerRequest.password.length < 8) {
            return Resource.Error("La contraseña debe tener al menos 8 caracteres.")
        }

        if (registerRequest.password != confirmPassword) {
            return Resource.Error("Las contraseñas no coinciden.")
        }

        return repository.register(registerRequest)
    }

}