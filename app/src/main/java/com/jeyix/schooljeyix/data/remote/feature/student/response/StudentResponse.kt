package com.jeyix.schooljeyix.data.remote.feature.student.response

data class StudentResponse(
    val id: Long,
    val gradeLevel: String,
    val section: String,
    val parentId: Long?,
    val parentName: String?,
    val user: UserSummary,
    val active: Boolean
)