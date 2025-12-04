package com.jeyix.schooljeyix.ui.admin.users.student.form

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.remote.feature.student.request.CreateStudentRequest
import com.jeyix.schooljeyix.data.remote.feature.student.request.UpdateStudentRequest
import com.jeyix.schooljeyix.domain.usecase.grade.GradeUseCases
import com.jeyix.schooljeyix.domain.usecase.parent.ParentUseCases
import com.jeyix.schooljeyix.domain.usecase.section.SectionUseCases
import com.jeyix.schooljeyix.domain.usecase.student.CreateStudentUseCase
import com.jeyix.schooljeyix.domain.usecase.student.GetStudentByIdUseCase
import com.jeyix.schooljeyix.domain.usecase.student.UpdateStudentProfileImageUseCase
import com.jeyix.schooljeyix.domain.usecase.student.UpdateStudentUseCase

import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminStudentFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getStudentUseCase: GetStudentByIdUseCase,
    private val createStudentUseCase: CreateStudentUseCase,
    private val updateStudentUseCase: UpdateStudentUseCase,
    private val parentUseCases: ParentUseCases,
    private val gradeUseCases: GradeUseCases,
    private val sectionUseCases: SectionUseCases,
    private val updateStudentProfileImageUseCase: UpdateStudentProfileImageUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(AdminStudentFormUiState())
    val uiState = _uiState.asStateFlow()

    private val studentId: String? = savedStateHandle.get("studentId")

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val parentsResult = parentUseCases.getAllParents()
            if (parentsResult is Resource.Success) {
                _uiState.update { it.copy(parentList = parentsResult.data ?: emptyList()) }
            } else {
                _uiState.update { it.copy(error = "No se pudieron cargar los apoderados") }
            }

            val gradesResult = gradeUseCases.getAllGrades()
            if (gradesResult is Resource.Success) {
                _uiState.update { it.copy(gradeList = gradesResult.data ?: emptyList()) }
            } else {
                _uiState.update { it.copy(error = "No se pudieron cargar los grados") }
            }

            if (studentId != null) {
                when(val studentResult = getStudentUseCase(studentId.toLong())) {
                    is Resource.Success -> {
                        val student = studentResult.data!!
                        _uiState.update { it.copy(studentDetails = student) }

                        val studentGrade = (gradesResult as? Resource.Success)?.data?.find {
                            it.name == student.gradeLevel
                        }
                        if (studentGrade != null) {
                            loadSections(studentGrade.id)
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(error = studentResult.message) }
                    }
                    else -> {}
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onGradeSelected(gradeId: Long) {
        _uiState.update { it.copy(sectionList = emptyList()) }
        viewModelScope.launch {
            loadSections(gradeId)
        }
    }

    private suspend fun loadSections(gradeId: Long) {
        when(val sectionsResult = sectionUseCases.getSectionsByGradeId(gradeId)) {
            is Resource.Success -> {
                _uiState.update { it.copy(sectionList = sectionsResult.data ?: emptyList()) }
            }
            is Resource.Error -> {
                _uiState.update { it.copy(error = "No se pudieron cargar las secciones") }
            }
            else -> {}
        }
    }

    fun saveStudent(request: CreateStudentRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            when (val result = createStudentUseCase(request)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isSaving = false, finishSaving = true) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isSaving = false, error = result.message) }
                }
                else -> {}
            }
        }
    }

    fun updateStudent(request: UpdateStudentRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            if (studentId == null) {
                _uiState.update { it.copy(isSaving = false, error = "ID de estudiante nulo") }
                return@launch
            }

            when (val result = updateStudentUseCase(studentId.toLong(), request)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isSaving = false, finishSaving = true) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isSaving = false, error = result.message) }
                }
                else -> {}
            }
        }
    }


    fun errorShown() {
        _uiState.update { it.copy(error = null) }
    }

    fun updateProfileImage(imageUri: Uri) {
        if (studentId == null) {
            _uiState.update { it.copy(error = "Debe guardar el estudiante antes de añadir una foto.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            when (val result = updateStudentProfileImageUseCase(studentId.toLong(), imageUri)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            studentDetails = result.data,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isSaving = false, error = result.message) }
                }
                else -> {
                    _uiState.update { it.copy(isSaving = false) }
                }
            }
        }
    }

    fun onFormPopulated() {
        _uiState.update { it.copy(isFormPopulated = true) }
    }

}
