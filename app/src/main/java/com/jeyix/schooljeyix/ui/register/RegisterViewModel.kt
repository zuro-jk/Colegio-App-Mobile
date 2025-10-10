package com.jeyix.schooljeyix.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.remote.feature.auth.request.RegisterRequest
import com.jeyix.schooljeyix.domain.usecase.auth.RegisterUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState = _registerState.asStateFlow()

    fun onRegisterClicked(
        registerRequest: RegisterRequest,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            val result = registerUseCase(
                registerRequest,
                confirmPassword = confirmPassword
            )

            when (result) {
                is Resource.Success -> {
                    _registerState.value = RegisterState.Success("¡Registro exitoso! Ya puedes iniciar sesión.")
                }
                is Resource.Error -> {
                    _registerState.value = RegisterState.Error(result.message)
                }
                else -> {}
            }
        }
    }
}