package com.jeyix.schooljeyix.ui.parent.students

import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse

data class StudentsUiState(
    val isLoading: Boolean = true,
    val students: List<StudentResponse> = emptyList(),
    val error: String? = null
)