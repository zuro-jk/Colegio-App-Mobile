package com.jeyix.schooljeyix.ui.admin.users.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse
import com.jeyix.schooljeyix.domain.usecase.users.UserUseCases
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminStaffListViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminStaffListUiState())
    val uiState = _uiState.asStateFlow()

    private var fullStaffList: List<UserProfileResponse> = emptyList()

    init {
        loadStaff()
    }

    fun onResume() {
        loadStaff()
    }

    fun loadStaff() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = userUseCases.getAllPersonalForAdmin()) {
                is Resource.Success -> {
                    fullStaffList = result.data ?: emptyList()
                    _uiState.update {
                        it.copy(isLoading = false, staff = fullStaffList)
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    /**
     * Filtra la lista 'fullStaffList' basado en la consulta
     * y actualiza el 'staff' en el UI state.
     */
    fun search(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(staff = fullStaffList) }
            return
        }

        val filteredList = fullStaffList.filter { user ->
            (user.fullName?.contains(query, ignoreCase = true) == true) ||
                    (user.email?.contains(query, ignoreCase = true) == true) ||
                    (user.username?.contains(query, ignoreCase = true) == true)
        }

        _uiState.update { it.copy(staff = filteredList) }
    }
}