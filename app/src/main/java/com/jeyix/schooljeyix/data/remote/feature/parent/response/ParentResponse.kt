package com.jeyix.schooljeyix.data.remote.feature.parent.response

import com.jeyix.schooljeyix.data.remote.feature.parent.response.detail.StudentSummary


data class ParentResponse(
    val id: Long,
    val user: UserResponseSummary,
    val children: List<StudentSummary>
)

