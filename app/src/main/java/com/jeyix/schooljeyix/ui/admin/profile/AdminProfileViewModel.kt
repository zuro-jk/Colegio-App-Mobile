package com.jeyix.schooljeyix.ui.admin.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.local.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            userPreferences.user.collect { storedUser ->
                if (storedUser != null) {
                    _uiState.update {
                        it.copy(isLoading = false, user = storedUser)
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, error = "No se encontró sesión activa")
                    }
                }
            }
        }
    }
}