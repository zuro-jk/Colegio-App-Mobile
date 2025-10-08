package com.jeyix.schooljeyix.domain.usecase

import com.jeyix.schooljeyix.domain.model.User

class AuthUseCases(private val repository: AuthRepository) {
    suspend fun login(usernameOrEmail: String, password: String) = repository.login(usernameOrEmail, password)
    suspend fun register(user: User) = repository.register(user)
}