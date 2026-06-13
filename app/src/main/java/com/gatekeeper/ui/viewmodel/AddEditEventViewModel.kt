package com.gatekeeper.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gatekeeper.data.model.EventContact
import com.gatekeeper.data.model.GateEvent
import com.gatekeeper.data.repository.EventRepository
import com.gatekeeper.util.PhoneContact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditEventUiState(
    val name: String = "",
    val startEpochMs: Long = System.currentTimeMillis(),
    val endEpochMs: Long = System.currentTimeMillis() + 3_600_000L, // +1 hour
    val assignedContacts: List<PhoneContact> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

@HiltViewModel
class AddEditEventViewModel @Inject constructor(
    private val repository: EventRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val eventId: Long = savedStateHandle["eventId"] ?: 0L

    private val _uiState = MutableStateFlow(AddEditEventUiState())
    val uiState: StateFlow<AddEditEventUiState> = _uiState.asStateFlow()

    init {
        if (eventId > 0L) loadEvent(eventId)
    }

    private fun loadEvent(id: Long) {
        viewModelScope.launch {
            val ewc = repository.getEventWithContacts(id) ?: return@launch
            _uiState.value = AddEditEventUiState(
                name = ewc.event.name,
                startEpochMs = ewc.event.startEpochMs,
                endEpochMs = ewc.event.endEpochMs,
                assignedContacts = ewc.contacts.map { PhoneContact(it.name, it.phoneNumber) }
            )
        }
    }

    fun onNameChange(value: String) { _uiState.value = _uiState.value.copy(name = value) }
    fun onStartChange(epochMs: Long) { _uiState.value = _uiState.value.copy(startEpochMs = epochMs) }
    fun onEndChange(epochMs: Long) { _uiState.value = _uiState.value.copy(endEpochMs = epochMs) }

    fun addContact(contact: PhoneContact) {
        val current = _uiState.value.assignedContacts
        if (current.none { it.phoneNumber == contact.phoneNumber }) {
            _uiState.value = _uiState.value.copy(assignedContacts = current + contact)
        }
    }

    fun removeContact(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(
            assignedContacts = _uiState.value.assignedContacts.filter { it.phoneNumber != phoneNumber }
        )
    }

    fun save() {
        val state = _uiState.value
        if (state.name.isBlank()) return
        viewModelScope.launch {
            val event = GateEvent(
                id = eventId,
                name = state.name.trim(),
                startEpochMs = state.startEpochMs,
                endEpochMs = state.endEpochMs
            )
            val contacts = state.assignedContacts.map {
                EventContact(eventId = eventId, name = it.name, phoneNumber = it.phoneNumber)
            }
            repository.saveEventWithContacts(event, contacts)
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }
}
