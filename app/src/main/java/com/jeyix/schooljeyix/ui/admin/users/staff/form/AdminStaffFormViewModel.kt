package com.jeyix.schooljeyix.ui.admin.users.staff.form

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.remote.feature.users.request.CreateAdminRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.UpdateUserRequest
import com.jeyix.schooljeyix.domain.usecase.users.UserUseCases
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminStaffFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminStaffFormUiState())
    val uiState = _uiState.asStateFlow()

    private val userId: Long = savedStateHandle.get<Long>("userId")!!

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        if (userId == 0L) {
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = userUseCases.getUserById(userId)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, userDetails = result.data) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                else -> {}
            }
        }
    }

    fun saveUser(request: CreateAdminRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            when (val result = userUseCases.createAdminUser(request)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isSaving = false, finishSaving = true) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isSaving = false, error = result.message) }
                }
                else -> {}
            }
        }
    }

    fun updateUser(request: UpdateUserRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            when (val result = userUseCases.updateUserById(userId, request)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isSaving = false, finishSaving = true) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isSaving = false, error = result.message) }
                }
                else -> {}
            }
        }
    }

    fun updateProfileImage(imageUri: Uri) {
        if (userId == 0L) {
            _uiState.update { it.copy(error = "Debe guardar el usuario antes de añadir una foto.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            when (val result = userUseCases.updateUserImageById(userId, imageUri)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isSaving = false, userDetails = result.data)
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isSaving = false, error = result.message) }
                }
                else -> { _uiState.update { it.copy(isSaving = false) } }
            }
        }
    }

    fun onFormPopulated() {
        _uiState.update { it.copy(isFormPopulated = true) }
    }

    fun errorShown() {
        _uiState.update { it.copy(error = null) }
    }
}