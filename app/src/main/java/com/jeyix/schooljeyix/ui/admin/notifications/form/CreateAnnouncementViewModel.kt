package com.jeyix.schooljeyix.ui.admin.notifications.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.domain.usecase.notifications.NotificationUseCases
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAnnouncementViewModel @Inject constructor(
    private val notificationUseCases: NotificationUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading(null))
    val uiState = _uiState.asStateFlow()

    fun sendAnnouncement(title: String, body: String, isAll: Boolean, isParents: Boolean, isStudents: Boolean) {
        viewModelScope.launch {
            _uiState.value = Resource.Loading()

            val targetRoles = mutableListOf<String>()
            if (isAll) {
                targetRoles.add("ALL")
            } else {
                if (isParents) targetRoles.add("ROLE_PARENT")
                if (isStudents) targetRoles.add("ROLE_STUDENT")
            }

            if (targetRoles.isEmpty()) {
                _uiState.value = Resource.Error("Debes seleccionar al menos un destinatario.")
                return@launch
            }

            val result = notificationUseCases.sendAnnouncement(
                title = title,
                body = body,
                targetRoles = targetRoles
            )

            _uiState.value = result
        }
    }

    fun resetState() {
        _uiState.value = Resource.Loading(null)
    }
}