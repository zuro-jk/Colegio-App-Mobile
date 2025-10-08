package com.jeyix.schooljeyix.data.remote.core

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)