package com.gatekeeper.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gatekeeper.data.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val gateContactName: String = "",
    val gatePhoneNumber: String = "",
    val selectedTab: Int = 0
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val prefs: AppPreferences
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(0)

    val uiState: StateFlow<MainUiState> = combine(
        prefs.gateContactName,
        prefs.gatePhoneNumber,
        _selectedTab
    ) { name, phone, tab ->
        MainUiState(gateContactName = name, gatePhoneNumber = phone, selectedTab = tab)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MainUiState())

    fun selectTab(index: Int) { _selectedTab.value = index }

    fun openGate(context: Context) {
        viewModelScope.launch {
            val phone = prefs.gatePhoneNumber.first()
            if (phone.isBlank()) return@launch
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }
}
