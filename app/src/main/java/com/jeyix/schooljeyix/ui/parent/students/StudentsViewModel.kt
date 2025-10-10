package com.jeyix.schooljeyix.ui.parent.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.domain.usecase.student.GetMyStudentsUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class StudentsViewModel @Inject constructor(
    private val getMyStudentsUseCase: GetMyStudentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStudents()
    }

    fun loadStudents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getMyStudentsUseCase()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, students = result.data ?: emptyList())
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = result.message)
                    }
                }
                is Resource.Loading -> { /* Ya estamos en estado de carga */ }
            }
        }
    }
}