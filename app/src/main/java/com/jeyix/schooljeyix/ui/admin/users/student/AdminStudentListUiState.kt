package com.jeyix.schooljeyix.ui.admin.users.student

import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse

data class AdminStudentListUiState(
    val isLoading: Boolean = true,
    val students: List<StudentResponse> = emptyList(),
    val error: String? = null
)