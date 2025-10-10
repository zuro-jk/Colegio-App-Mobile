package com.jeyix.schooljeyix.data.remote.feature.student.request

data class StudentRequest (
    val userId: Long,
    val parentId: Long,
    val gradeLevel: String,
    val section: String
)

