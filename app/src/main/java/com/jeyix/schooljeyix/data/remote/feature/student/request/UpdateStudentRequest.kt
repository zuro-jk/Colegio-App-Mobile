package com.jeyix.schooljeyix.data.remote.feature.student.request

data class UpdateStudentRequest (
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String?,
    val parentId: Long,
    val sectionId: Long
)