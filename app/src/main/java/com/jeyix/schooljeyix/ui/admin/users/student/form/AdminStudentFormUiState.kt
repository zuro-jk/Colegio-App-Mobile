package com.jeyix.schooljeyix.ui.admin.users.student.form

import com.jeyix.schooljeyix.data.remote.feature.grade.response.GradeResponse
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.data.remote.feature.section.response.SectionResponse
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse

data class AdminStudentFormUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val studentDetails: StudentResponse? = null,

    val parentList: List<ParentResponse> = emptyList(),
    val gradeList: List<GradeResponse> = emptyList(),
    val sectionList: List<SectionResponse> = emptyList(),

    val error: String? = null,
    val finishSaving: Boolean = false,

    val isFormPopulated: Boolean = false
)