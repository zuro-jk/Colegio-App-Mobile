package com.jeyix.schooljeyix.domain.usecase.grade

import jakarta.inject.Inject

data class GradeUseCases @Inject constructor(
    val createGrade: CreateGradeUseCase,
    val getAllGrades: GetAllGradesUseCase,
    val getGradeById: GetGradeByIdUseCase,
    val updateGrade: UpdateGradeUseCase,
    val deleteGrade: DeleteGradeUseCase
)