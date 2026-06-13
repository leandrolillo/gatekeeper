package com.gatekeeper.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gatekeeper.data.model.AuthorizedContact
import com.gatekeeper.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    val contacts: StateFlow<List<AuthorizedContact>> = repository.getAllContacts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun deleteContact(contact: AuthorizedContact) {
        viewModelScope.launch { repository.deleteContact(contact) }
    }

    fun toggleEnabled(contact: AuthorizedContact) {
        viewModelScope.launch {
            repository.updateContact(contact.copy(isEnabled = !contact.isEnabled))
        }
    }
}
