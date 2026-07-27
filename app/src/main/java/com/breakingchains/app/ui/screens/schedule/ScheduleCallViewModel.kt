package com.breakingchains.app.ui.screens.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breakingchains.app.data.repository.AuthRepository
import com.breakingchains.app.data.repository.CallRequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ScheduleCallUiState(
    val preferredDate: String = "2026-07-28",
    val preferredTime: String = "14:00",
    val reasonNote: String = "",
    val isLoading: Boolean = false,
    val isSubmittedSuccessfully: Boolean = false,
    val errorMessage: String? = null
)

class ScheduleCallViewModel(
    private val authRepository: AuthRepository,
    private val callRequestRepository: CallRequestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleCallUiState())
    val uiState: StateFlow<ScheduleCallUiState> = _uiState.asStateFlow()

    fun onDateChanged(date: String) {
        _uiState.update { it.copy(preferredDate = date, errorMessage = null) }
    }

    fun onTimeChanged(time: String) {
        _uiState.update { it.copy(preferredTime = time, errorMessage = null) }
    }

    fun onReasonNoteChanged(note: String) {
        _uiState.update { it.copy(reasonNote = note, errorMessage = null) }
    }

    fun submitScheduleCall(onSuccess: () -> Unit) {
        val user = authRepository.currentUser.value
        val userId = user?.id ?: "u_guest"
        val userName = user?.name ?: "Anonymous User"
        val userEmail = user?.email ?: "user@example.com"
        val state = _uiState.value

        if (state.reasonNote.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter a brief note for the call.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                callRequestRepository.scheduleCall(
                    userId = userId,
                    userName = userName,
                    userEmail = userEmail,
                    preferredDate = state.preferredDate,
                    preferredTime = state.preferredTime,
                    note = state.reasonNote.trim()
                )
                _uiState.update { it.copy(isLoading = false, isSubmittedSuccessfully = true) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to schedule call request."
                    )
                }
            }
        }
    }
}
