package com.jeyix.schooljeyix.ui.admin.enrollments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentResponse
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentStatus
import com.jeyix.schooljeyix.domain.usecase.enrollment.GetAllEnrollmentsUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnrollmentsViewModel @Inject constructor(
    private val getAllEnrollmentsUseCase: GetAllEnrollmentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EnrollmentsUiState())
    val uiState = _uiState.asStateFlow()
    private var allEnrollmentsCache: List<EnrollmentResponse> = emptyList()
    private var currentSearchQuery: String = ""
    private var currentStatusFilter: EnrollmentStatus? = null

    init {
        loadEnrollments()
    }

    fun loadEnrollments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getAllEnrollmentsUseCase()) {
                is Resource.Success -> {
                    allEnrollmentsCache = result.data ?: emptyList()
                    applyFilters()
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        currentSearchQuery = query
        applyFilters()
    }

    fun onFilterChanged(status: EnrollmentStatus?) {
        currentStatusFilter = status
        applyFilters()
    }

    private fun applyFilters() {
        val filteredList = allEnrollmentsCache.filter { enrollment ->
            val matchesSearch = enrollment.student.fullName.contains(currentSearchQuery, ignoreCase = true)

            val matchesStatus = if (currentStatusFilter == null) {
                true
            } else {
                enrollment.status == currentStatusFilter
            }

            matchesSearch && matchesStatus
        }

        _uiState.update {
            it.copy(isLoading = false, enrollments = filteredList)
        }
    }
}