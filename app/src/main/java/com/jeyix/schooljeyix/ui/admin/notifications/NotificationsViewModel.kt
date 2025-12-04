package com.jeyix.schooljeyix.ui.admin.notifications

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeyix.schooljeyix.domain.model.Announcement
import com.jeyix.schooljeyix.domain.usecase.notifications.NotificationUseCases
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationUseCases: NotificationUseCases
) : ViewModel() {
    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements = _announcements.asStateFlow()

    fun refreshAnnouncements() {
        loadAnnouncements()
    }

    private fun loadAnnouncements() {
        viewModelScope.launch {
            when(val result = notificationUseCases.getAnnouncements()) {
                is Resource.Success -> {
                    _announcements.value = result.data ?: emptyList()
                }
                is Resource.Error -> {
                }
                else -> {}
            }
        }
    }
}