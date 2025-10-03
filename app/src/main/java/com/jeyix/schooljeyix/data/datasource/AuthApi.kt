package com.jeyix.schooljeyix.data.datasource

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<Boolean>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<Boolean>
}