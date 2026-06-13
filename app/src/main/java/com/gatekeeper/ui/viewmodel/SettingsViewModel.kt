package com.gatekeeper.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gatekeeper.data.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val gateContactName: String = "",
    val gatePhoneNumber: String = "",
    val keywords: Set<String> = AppPreferences.DEFAULT_KEYWORDS,
    val monitoringEnabled: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: AppPreferences
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        prefs.gateContactName,
        prefs.gatePhoneNumber,
        prefs.triggerKeywords,
        prefs.monitoringEnabled
    ) { name, phone, keywords, monitoring ->
        SettingsUiState(gateContactName = name, gatePhoneNumber = phone,
            keywords = keywords, monitoringEnabled = monitoring)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun saveGateContact(name: String, phone: String) {
        viewModelScope.launch { prefs.setGateContact(name, phone) }
    }

    fun addKeyword(keyword: String) {
        if (keyword.isBlank()) return
        viewModelScope.launch {
            val current = prefs.triggerKeywords.first()
            prefs.setTriggerKeywords(current + keyword.trim().lowercase())
        }
    }

    fun removeKeyword(keyword: String) {
        viewModelScope.launch {
            val current = prefs.triggerKeywords.first()
            prefs.setTriggerKeywords(current - keyword)
        }
    }

    fun setMonitoringEnabled(enabled: Boolean) {
        viewModelScope.launch { prefs.setMonitoringEnabled(enabled) }
    }
}
