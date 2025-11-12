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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminEditParentViewModel @Inject constructor(
    private val parentUseCases: ParentUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminEditParentUiState())
    val uiState = _uiState.asStateFlow()

    // Obtiene el ID. Si es 0L (valor por defecto), es "Modo Crear".
    private val parentId: Long = savedStateHandle.get<Long>("parentId")!!

    init {
        if (parentId == 0L) {
            // Modo Crear: No cargamos nada, solo quitamos el 'loading'
            _uiState.update { it.copy(isLoading = false) }
        } else {
            // Modo Editar: Cargamos los datos
            loadParentData()
        }
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

    /**
     * Llama al UseCase para crear un nuevo padre.
     */
    fun createParent(request: CreateParentRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (parentUseCases.addParent(request)) { // Asumiendo que tu UseCase se llama 'createParent'
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, isUpdateSuccessful = true) }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = "Error al crear") }
                else -> {}
            }
        }
    }

    /**
     * Llama al UseCase para actualizar un padre existente.
     */
    fun updateParent(request: UpdateParentRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (parentUseCases.updateParent(parentId, request)) {
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, isUpdateSuccessful = true) }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = "Error al guardar") }
                else -> {}
            }
        }
    }

    /**
     * Sube la imagen de perfil para el padre actual (solo en modo edición).
     */
    fun updateProfileImage(imageUri: Uri) {
        if (parentId == 0L) {
            _uiState.update { it.copy(error = "Guarda el padre primero") }
            return
        }
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

    /**
     * El Fragment llama a esto después de 'populateForm' para evitar que se repita.
     */
    fun onFormPopulated() {
        _uiState.update { it.copy(isFormPopulated = true) }
    }

    /**
     * El Fragment llama a esto después de mostrar un Toast de error.
     */
    fun errorShown() {
        _uiState.update { it.copy(error = null) }
    }
}