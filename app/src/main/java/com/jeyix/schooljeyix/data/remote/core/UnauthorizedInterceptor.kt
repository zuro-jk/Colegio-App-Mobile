package com.jeyix.schooljeyix.data.remote.core

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.jeyix.schooljeyix.data.local.datastore.UserPreferences
import com.jeyix.schooljeyix.domain.usecase.auth.LogoutUseCase
import com.jeyix.schooljeyix.ui.login.LoginActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
class UnauthorizedInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferences: UserPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.code == 401) {
            runBlocking {
                userPreferences.clear()
            }

            Handler(Looper.getMainLooper()).post {
                val intent = Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            }
        }

        return response
    }
}