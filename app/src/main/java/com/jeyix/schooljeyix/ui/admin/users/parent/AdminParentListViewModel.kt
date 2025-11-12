package com.jeyix.schooljeyix.ui.admin.users.parent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.domain.usecase.parent.ParentUseCases
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminParentListViewModel @Inject constructor(
    private val parentUseCases: ParentUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminParentListUiState())
    val uiState = _uiState.asStateFlow()

    // --- AÑADIDO ---
    private var fullParentList: List<ParentResponse> = emptyList()
    // ---

    init {
        loadParents()
    }

    fun loadParents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = parentUseCases.getAllParents()) {
                is Resource.Success -> {
                    fullParentList = result.data ?: emptyList()
                    _uiState.update {
                        it.copy(isLoading = false, parents = fullParentList)
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(parents = fullParentList) }
            return
        }
        val filteredList = fullParentList.filter { parent ->
            (parent.user.fullName.contains(query, ignoreCase = true)) ||
                    (parent.user.email.contains(query, ignoreCase = true)) ||
                    (parent.user.username.contains(query, ignoreCase = true))
        }
        _uiState.update { it.copy(parents = filteredList) }
    }
}