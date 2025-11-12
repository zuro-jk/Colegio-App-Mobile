package com.jeyix.schooljeyix.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.local.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _authState = MutableStateFlow<SplashAuthState>(SplashAuthState.Loading)
    val authState = _authState.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val token = userPreferences.accessToken.first()
            val user = userPreferences.user.first()

            if (!token.isNullOrBlank() && user != null) {
                val roles = user.roles
                val primaryRole = when {
                    (roles?.contains("ROLE_ADMIN") ?: false) -> "ROLE_ADMIN"
                    (roles?.contains("ROLE_TEACHER") ?: false) -> "ROLE_TEACHER"
                    (roles?.contains("ROLE_PARENT") ?: false) -> "ROLE_PARENT"
                    (roles?.contains("ROLE_STUDENT") ?: false) -> "ROLE_STUDENT"
                    else -> "UNKNOWN"
                }
                _authState.value = SplashAuthState.Authenticated(primaryRole)
            } else {
                _authState.value = SplashAuthState.Unauthenticated
            }
        }
    }
}