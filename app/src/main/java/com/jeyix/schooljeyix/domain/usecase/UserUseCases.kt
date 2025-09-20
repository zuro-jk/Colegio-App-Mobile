package com.jeyix.schooljeyix.domain.usecase

import com.jeyix.schooljeyix.domain.model.User

class UserUseCases(private val repository: UserRepository) {
    suspend fun login(email: String, password: String) = repository.login(email, password)
    suspend fun register(user: User) = repository.register(user)
}