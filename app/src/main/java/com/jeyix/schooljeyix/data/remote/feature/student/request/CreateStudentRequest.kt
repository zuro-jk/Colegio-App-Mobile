package com.jeyix.schooljeyix.data.remote.feature.student.request

data class CreateStudentRequest (
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String?,
    val parentId: Long,
    val sectionId: Long
)
