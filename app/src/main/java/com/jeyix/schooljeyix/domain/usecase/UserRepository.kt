package com.jeyix.schooljeyix.domain.usecase

import com.jeyix.schooljeyix.domain.model.User

interface UserRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(user: User): Boolean
}