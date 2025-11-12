package com.jeyix.schooljeyix.domain.usecase.section

import javax.inject.Inject


data class SectionUseCases @Inject constructor(
    val createSection: CreateSectionUseCase,
    val getSectionsByGradeId: GetSectionsByGradeIdUseCase,
    val updateSection: UpdateSectionUseCase,
    val deleteSection: DeleteSectionUseCase
)