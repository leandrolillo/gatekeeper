package com.gatekeeper.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gatekeeper.data.model.AuthorizedContact
import com.gatekeeper.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditContactUiState(
    val name: String = "",
    val phoneNumber: String = "",
    val activeDays: Int = 0b1111111,
    val startTimeMinutes: Int = 480,  // 08:00
    val endTimeMinutes: Int = 1320,   // 22:00
    val isEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

@HiltViewModel
class AddEditContactViewModel @Inject constructor(
    private val repository: ContactRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val contactId: Long = savedStateHandle["contactId"] ?: 0L

    private val _uiState = MutableStateFlow(AddEditContactUiState())
    val uiState: StateFlow<AddEditContactUiState> = _uiState.asStateFlow()

    init {
        if (contactId > 0L) loadContact(contactId)
    }

    private fun loadContact(id: Long) {
        viewModelScope.launch {
            val contact = repository.getContactById(id) ?: return@launch
            _uiState.value = AddEditContactUiState(
                name = contact.name,
                phoneNumber = contact.phoneNumber,
                activeDays = contact.activeDays,
                startTimeMinutes = contact.startTimeMinutes,
                endTimeMinutes = contact.endTimeMinutes,
                isEnabled = contact.isEnabled
            )
        }
    }

    fun onNameChange(value: String) { _uiState.value = _uiState.value.copy(name = value) }
    fun onPhoneChange(value: String) { _uiState.value = _uiState.value.copy(phoneNumber = value) }
    fun onActiveDaysChange(value: Int) { _uiState.value = _uiState.value.copy(activeDays = value) }
    fun onStartTimeChange(minutes: Int) { _uiState.value = _uiState.value.copy(startTimeMinutes = minutes) }
    fun onEndTimeChange(minutes: Int) { _uiState.value = _uiState.value.copy(endTimeMinutes = minutes) }
    fun onEnabledChange(value: Boolean) { _uiState.value = _uiState.value.copy(isEnabled = value) }

    fun save() {
        val state = _uiState.value
        if (state.name.isBlank() || state.phoneNumber.isBlank()) return
        viewModelScope.launch {
            val contact = AuthorizedContact(
                id = contactId,
                name = state.name.trim(),
                phoneNumber = state.phoneNumber.trim(),
                activeDays = state.activeDays,
                startTimeMinutes = state.startTimeMinutes,
                endTimeMinutes = state.endTimeMinutes,
                isEnabled = state.isEnabled
            )
            repository.saveContact(contact)
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }
}
