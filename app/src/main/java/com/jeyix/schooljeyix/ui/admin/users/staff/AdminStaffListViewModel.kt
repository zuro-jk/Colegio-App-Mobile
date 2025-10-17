package com.jeyix.schooljeyix.ui.admin.users.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.domain.usecase.users.UserUseCases
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AdminStaffListViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminStaffListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStaff()
    }

    private fun loadStaff() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Suponiendo que tienes un endpoint y un UseCase para traer todo el personal
            when (val result = userUseCases.getAllPersonalForAdmin()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, staff = result.data ?: emptyList())
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