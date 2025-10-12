package com.jeyix.schooljeyix.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.local.datastore.UserPreferences
import com.jeyix.schooljeyix.data.remote.feature.auth.request.LoginRequest
import com.jeyix.schooljeyix.domain.usecase.auth.LoginUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val userPreferences: UserPreferences
): ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val result = loginUseCase(loginRequest)

            when (result) {
                is Resource.Success -> {
                    val authData = result.data!!
                    userPreferences.saveUserData(
                        accessToken = authData.accessToken,
                        sessionId = authData.sessionId,
                        user = authData.user
                    )

                    val roles = authData.user.roles
                    val primaryRole = when {
                        (roles?.contains("ROLE_ADMIN") ?: false) -> "ROLE_ADMIN"
                        (roles?.contains("ROLE_TEACHER") ?: false) -> "ROLE_TEACHER"
                        (roles?.contains("ROLE_PARENT") ?: false) -> "ROLE_PARENT"
                        (roles?.contains("ROLE_STUDENT") ?: false) -> "ROLE_STUDENT"
                        else -> "UNKNOWN"
                    }

                    _loginState.value = LoginState.Success(primaryRole)
                }
                is Resource.Error -> {
                    _loginState.value = LoginState.Error(result.message)
                }
                else -> {
                }
            }
        }
    }
}