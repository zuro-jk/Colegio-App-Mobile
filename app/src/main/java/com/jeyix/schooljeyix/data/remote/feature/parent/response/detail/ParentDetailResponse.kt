package com.jeyix.schooljeyix.data.remote.feature.parent.response.detail

data class ParentDetailResponse (
    val id: Long,
    val user: UserDetailSummary,
    val children: List<StudentSummary>
)