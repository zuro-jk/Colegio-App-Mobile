package com.jeyix.schooljeyix.ui.parent.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.domain.usecase.student.GetMyStudentsUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentsViewModel @Inject constructor(
    private val getMyStudentsUseCase: GetMyStudentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentsUiState())
    val uiState = _uiState.asStateFlow()

    private var allStudentsCache: List<StudentResponse> = emptyList()

    init {
        loadStudents()
    }

    fun loadStudents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getMyStudentsUseCase()) {
                is Resource.Success -> {
                    val students = result.data ?: emptyList()
                    allStudentsCache = students
                    _uiState.update {
                        it.copy(isLoading = false, students = students)
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = result.message)
                    }
                }
                is Resource.Loading -> { }
            }
        }
    }

    fun searchStudent(query: String) {
        val filteredList = if (query.isEmpty()) {
            allStudentsCache
        } else {
            allStudentsCache.filter {
                it.user.fullName.contains(query, ignoreCase = true)
            }
        }
        _uiState.update { it.copy(students = filteredList) }
    }
}