package com.jeyix.schooljeyix.data.remote.feature.student.response

data class StudentResponse (
    val id: Long,
    val username: String,
    val fullName: String,
    val gradeLevel: String,
    val section: String,
    val parentId: Long,
    val parentName: String
)
