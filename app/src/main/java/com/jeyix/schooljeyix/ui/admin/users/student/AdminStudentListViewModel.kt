package com.jeyix.schooljeyix.ui.admin.users.student

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.domain.usecase.student.GetAllStudentsUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AdminStudentListViewModel @Inject constructor(
    private val getAllStudentsUseCase: GetAllStudentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminStudentListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStudents()
    }

    private fun loadStudents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = getAllStudentsUseCase()
            Log.d("AdminUsersDebug", "Resultado del GetAllStudentsUseCase: $result")
            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, students = result.data ?: emptyList())
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