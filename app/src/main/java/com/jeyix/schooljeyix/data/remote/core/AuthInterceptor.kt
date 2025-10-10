package com.jeyix.schooljeyix.data.remote.core

import com.jeyix.schooljeyix.data.local.datastore.UserPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = runBlocking {
            userPreferences.accessToken.first()
        }

        val newRequest = if (token != null && token.isNotBlank()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }

}