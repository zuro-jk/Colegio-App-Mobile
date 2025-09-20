package com.jeyix.schooljeyix.data.repository

import com.jeyix.schooljeyix.domain.model.User
import com.jeyix.schooljeyix.domain.usecase.UserRepository

class UserRepositoryImpl: UserRepository {

    private val users = mutableListOf<User>()

    override suspend fun login(email: String, password: String): Boolean {
        return users.any { it.email == email && it.password == password }
    }

    override suspend fun register(user: User): Boolean {
        return if (users.any { it.email == user.email}) {
            false
        } else {
            users.add(user)
            true
        }
    }

}