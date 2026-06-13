package com.gatekeeper.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import com.gatekeeper.util.MessageMatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "gatekeeper_prefs")

@Singleton
class AppPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private val KEY_GATE_PHONE = stringPreferencesKey("gate_phone_number")
        private val KEY_GATE_CONTACT_NAME = stringPreferencesKey("gate_contact_name")
        private val KEY_TRIGGER_KEYWORDS = stringSetPreferencesKey("trigger_keywords")
        private val KEY_MONITORING_ENABLED = booleanPreferencesKey("monitoring_enabled")

        // Default set pre-populated with all built-in stems and phrases.
        // null in DataStore = never written = use defaults.
        // Empty set = user intentionally cleared everything.
        val DEFAULT_KEYWORDS: Set<String> =
            (MessageMatcher.BUILT_IN_STEMS + MessageMatcher.BUILT_IN_PHRASES).toSet()
    }

    val gatePhoneNumber: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_GATE_PHONE] ?: ""
    }

    val gateContactName: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_GATE_CONTACT_NAME] ?: ""
    }

    val triggerKeywords: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[KEY_TRIGGER_KEYWORDS] ?: DEFAULT_KEYWORDS
    }

    val monitoringEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_MONITORING_ENABLED] ?: true
    }

    suspend fun setGatePhoneNumber(number: String) {
        context.dataStore.edit { it[KEY_GATE_PHONE] = number }
    }

    suspend fun setGateContact(name: String, phone: String) {
        context.dataStore.edit {
            it[KEY_GATE_CONTACT_NAME] = name
            it[KEY_GATE_PHONE] = phone
        }
    }

    suspend fun setTriggerKeywords(keywords: Set<String>) {
        context.dataStore.edit { it[KEY_TRIGGER_KEYWORDS] = keywords }
    }

    suspend fun setMonitoringEnabled(enabled: Boolean) {
        context.dataStore.edit { it[KEY_MONITORING_ENABLED] = enabled }
    }
}
