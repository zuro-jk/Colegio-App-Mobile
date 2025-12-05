package com.jeyix.schooljeyix.ui.admin.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.local.datastore.UserPreferences
import com.jeyix.schooljeyix.data.remote.feature.users.request.UpdateProfileRequest
import com.jeyix.schooljeyix.domain.usecase.users.UserUseCases
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AdminEditProfileViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState = _uiState.asStateFlow()

    val currentUser = userPreferences.user

    fun updateProfile(firstName: String, lastName: String, username: String, email: String, phone: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val request = UpdateProfileRequest(
                firstName = firstName,
                lastName = lastName,
                username = username,
                email = email,
                phone = phone
            )

            when (val result = userUseCases.updateProfile(request)) {
                is Resource.Success -> {
                    val currentToken = userPreferences.accessToken.first()
                    val currentSession = userPreferences.sessionId.first()

                    refreshLocalUserData(currentToken, currentSession)
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    private suspend fun refreshLocalUserData(token: String?, sessionId: Int?) {
        when (val meResult = userUseCases.getMeData()) {
            is Resource.Success -> {
                meResult.data?.let { updatedUser ->
                    userPreferences.saveUserData(token, sessionId, updatedUser)
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
            }
            is Resource.Error -> {
                _uiState.update { it.copy(isLoading = false, error = "Perfil actualizado, pero error al sincronizar localmente.") }
            }
            else -> {}
        }
    }
}