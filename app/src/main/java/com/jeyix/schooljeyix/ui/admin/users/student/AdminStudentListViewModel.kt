package com.jeyix.schooljeyix.ui.admin.users.student

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.domain.usecase.student.GetAllStudentsUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminStudentListViewModel @Inject constructor(
    private val getAllStudentsUseCase: GetAllStudentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminStudentListUiState())
    val uiState = _uiState.asStateFlow()

    private var fullStudentList: List<StudentResponse> = emptyList()

    init {
        loadStudents()
    }

    fun loadStudents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = getAllStudentsUseCase()
            Log.d("AdminUsersDebug", "Resultado del GetAllStudentsUseCase: $result")
            when (result) {
                is Resource.Success -> {
                    fullStudentList = result.data ?: emptyList()
                    _uiState.update {
                        it.copy(isLoading = false, students = fullStudentList)
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(students = fullStudentList) }
            return
        }
        val filteredList = fullStudentList.filter { student ->
            (student.user.fullName.contains(query, ignoreCase = true)) ||
                    (student.user.email.contains(query, ignoreCase = true)) ||
                    (student.user.username.contains(query, ignoreCase = true))
        }
        _uiState.update { it.copy(students = filteredList) }
    }
}