package com.jeyix.schooljeyix.domain.usecase.parent

import javax.inject.Inject


data class ParentUseCases @Inject constructor(
    val getAllParents: GetAllParentsUseCase,
    val getParentById: GetParentByIdUseCase,
    val getParentByIdDetails: GetParentByIdDetailsUseCase,
    val addParent: CreateParentUseCase,
    val updateParentProfileImage: UpdateParentProfileImageUseCase,
    val updateParent: UpdateParentUseCase,
    val deleteParent: DeleteParentUseCase
)