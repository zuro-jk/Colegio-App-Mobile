package com.jeyix.schooljeyix.ui.splash

sealed class SplashAuthState {
    object Loading : SplashAuthState()
    object Authenticated : SplashAuthState()
    object Unauthenticated : SplashAuthState()
}