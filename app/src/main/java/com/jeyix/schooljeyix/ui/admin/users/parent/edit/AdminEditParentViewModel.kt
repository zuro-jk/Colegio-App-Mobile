package com.jeyix.schooljeyix.ui.admin.users.parent.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.remote.feature.parent.request.CreateParentRequest
import com.jeyix.schooljeyix.data.remote.feature.parent.request.UpdateParentRequest
import com.jeyix.schooljeyix.domain.usecase.parent.ParentUseCases
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AdminEditParentViewModel @Inject constructor(
    private val parentUseCases: ParentUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminEditParentUiState())
    val uiState = _uiState.asStateFlow()

    private val parentId: Long = savedStateHandle.get<Long>("parentId")!!

    init {
        loadParentData()
    }

    private fun loadParentData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = parentUseCases.getParentByIdDetails(parentId)) {
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, parent = result.data) }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun saveChanges(updatedRequest: UpdateParentRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (parentUseCases.updateParent(parentId, updatedRequest)) {
                // Actualiza el 'parent' local con los nuevos datos
                is Resource.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        isUpdateSuccessful = true,
                        // Opcional: actualizar 'parent' con la respuesta de 'updateParent'
                        // parent = result.data
                    )
                }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = "Error al guardar") }
                else -> {}
            }
        }
    }

    fun updateProfileImage(imageUri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = parentUseCases.updateParentProfileImage(parentId, imageUri)) {
                is Resource.Success -> {

                    val currentUserDetails = _uiState.value.parent?.user

                    val newUserSummary = result.data?.user
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            parent = it.parent?.copy(
                                user = currentUserDetails!!.copy(
                                    profileImageUrl = newUserSummary?.profileImageUrl
                                )
                            )
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
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