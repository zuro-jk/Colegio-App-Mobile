package com.jeyix.schooljeyix.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.local.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


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

            if (!token.isNullOrBlank()) {
                _authState.value = SplashAuthState.Authenticated
            } else {
                _authState.value = SplashAuthState.Unauthenticated
            }
        }
    }
}