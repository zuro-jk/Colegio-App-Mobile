package com.jeyix.schooljeyix.data.repository

import com.jeyix.schooljeyix.data.datasource.LoginRequest
import com.jeyix.schooljeyix.data.datasource.RegisterRequest
import com.jeyix.schooljeyix.data.datasource.RetrofitInstance
import com.jeyix.schooljeyix.domain.model.User
import com.jeyix.schooljeyix.domain.usecase.UserRepository

class UserRepositoryImpl: UserRepository {

    private val api = RetrofitInstance.api

    override suspend fun login(usernameOrEmail: String, password: String): Boolean {
        val response = api.login(LoginRequest(usernameOrEmail, password))
        return response.isSuccessful && response.body() == true
    }

    override suspend fun register(user: User): Boolean {
        val request = RegisterRequest(
            firstName = user.firstName,
            lastName = user.lastName,
            username = user.username,
            email = user.email,
            password = user.password
        )
        val response = api.register(request)
        return response.isSuccessful && response.body() == true
    }

}