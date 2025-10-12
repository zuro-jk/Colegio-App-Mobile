package com.jeyix.schooljeyix.ui.login

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userRole: String) : LoginState()
    data class Error(val message: String?) : LoginState()
}