package com.jeyix.schooljeyix.ui.admin.users.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.domain.usecase.student.ActivateStudentUseCase
import com.jeyix.schooljeyix.domain.usecase.student.DeleteStudentUseCase
import com.jeyix.schooljeyix.domain.usecase.student.GetAllStudentsUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class StudentFilterType {
    ALL, ACTIVE, INACTIVE
}

@HiltViewModel
class AdminStudentListViewModel @Inject constructor(
    private val getAllStudentsUseCase: GetAllStudentsUseCase,
    private val deleteStudentUseCase: DeleteStudentUseCase,
    private val activateStudentUseCase: ActivateStudentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminStudentListUiState())
    val uiState = _uiState.asStateFlow()

    private var fullStudentList: List<StudentResponse> = emptyList()

    private var currentSearchQuery: String = ""
    private var currentFilterType: StudentFilterType = StudentFilterType.ALL

    init {
        loadStudents()
    }

    fun loadStudents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = getAllStudentsUseCase()

            when (result) {
                is Resource.Success -> {
                    fullStudentList = result.data ?: emptyList()
                    applyFilters()
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun activateStudent(studentId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = activateStudentUseCase(studentId)) {
                is Resource.Success -> loadStudents()
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                is Resource.Loading -> {}
            }
        }
    }

    fun deleteStudent(studentId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = deleteStudentUseCase(studentId)) {
                is Resource.Success -> loadStudents()
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                is Resource.Loading -> {}
            }
        }
    }

    fun search(query: String) {
        currentSearchQuery = query
        applyFilters()
    }

    fun setFilterType(filterType: StudentFilterType) {
        currentFilterType = filterType
        applyFilters()
    }

    private fun applyFilters() {
        var filteredList = fullStudentList

        filteredList = when (currentFilterType) {
            StudentFilterType.ALL -> filteredList
            StudentFilterType.ACTIVE -> filteredList.filter { it.active }
            StudentFilterType.INACTIVE -> filteredList.filter { !it.active }
        }

        if (currentSearchQuery.isNotBlank()) {
            filteredList = filteredList.filter { student ->
                student.user.fullName.contains(currentSearchQuery, ignoreCase = true) ||
                        student.user.email.contains(currentSearchQuery, ignoreCase = true) ||
                        student.user.username.contains(currentSearchQuery, ignoreCase = true)
            }
        }

        _uiState.update {
            it.copy(
                isLoading = false,
                students = filteredList
            )
        }
    }
}