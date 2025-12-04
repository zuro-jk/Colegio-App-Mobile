package com.jeyix.schooljeyix.ui.admin.dashboard

data class DashboardUiState(
    val isLoading: Boolean = true,
    val studentCount: Int = 0,
    val totalIncome: Double = 0.0,
    val pendingAlerts: Int = 0,
    val recentActivity: List<ActivityItem> = emptyList(),
    val chartData: List<Float> = emptyList(),
    val error: String? = null
)

data class ActivityItem(
    val title: String,
    val subtitle: String,
    val timestamp: String,
    val type: ActivityType
)

enum class ActivityType { NEW_STUDENT, PAYMENT, ALERT }