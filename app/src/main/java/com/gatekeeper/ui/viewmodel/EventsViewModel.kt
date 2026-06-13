package com.gatekeeper.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gatekeeper.data.model.EventWithContacts
import com.gatekeeper.data.model.GateEvent
import com.gatekeeper.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    val events: StateFlow<List<EventWithContacts>> = repository.getAllEventsWithContacts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun deleteEvent(event: GateEvent) {
        viewModelScope.launch { repository.deleteEvent(event) }
    }

    fun toggleEnabled(event: GateEvent) {
        viewModelScope.launch {
            repository.updateEvent(event.copy(isEnabled = !event.isEnabled))
        }
    }
}
