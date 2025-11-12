package com.jeyix.schooljeyix.data.remote.feature.grade.response

import com.jeyix.schooljeyix.data.remote.feature.section.response.SectionResponse

data class GradeDetailResponse(
    val id: Long,
    val name: String,
    val sections: List<SectionResponse>
)
