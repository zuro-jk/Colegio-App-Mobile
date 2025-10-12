package com.jeyix.schooljeyix.ui.splash

sealed class SplashAuthState {
    object Loading : SplashAuthState()
    data class Authenticated(val userRole: String) : SplashAuthState()
    object Unauthenticated : SplashAuthState()
}