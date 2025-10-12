package com.jeyix.schooljeyix.ui.admin.users.parentTab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.domain.usecase.parent.ParentUseCases
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AdminParentListViewModel @Inject constructor(
    private val parentUseCases: ParentUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminParentListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadParents()
    }

    private fun loadParents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = parentUseCases.getAllParents()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, parents = result.data ?: emptyList())
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }
}