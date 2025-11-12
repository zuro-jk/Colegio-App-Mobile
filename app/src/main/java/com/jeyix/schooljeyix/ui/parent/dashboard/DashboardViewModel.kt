package com.jeyix.schooljeyix.ui.parent.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.data.local.datastore.UserPreferences
import com.jeyix.schooljeyix.domain.usecase.dashboard.GetDashboardDataUseCase
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val getDashboardDataUseCase: GetDashboardDataUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()


    init {
        userPreferences.user
            .onEach { userProfile ->
                _uiState.update { currentState ->
                    currentState.copy(
                        parentName = userProfile?.firstName ?: "Usuario",
                        parentAvatarUrl = userProfile?.profileImageUrl,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)

        loadRemoteData()
    }

    fun loadRemoteData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = getDashboardDataUseCase()
            Log.d("DashboardDebug", "Resultado del GetDashboardDataUseCase: $result")


            when (result) {
                is Resource.Success -> {
                    val remoteData = result.data!!
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nextPayment = remoteData.nextPayment,
                            overduePaymentsCount = remoteData.overduePaymentsCount,
                            students = remoteData.students
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }
}