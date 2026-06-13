package com.gatekeeper.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import com.gatekeeper.R
import com.gatekeeper.util.MessageMatcher
import com.gatekeeper.data.model.AuthorizedContact
import com.gatekeeper.data.preferences.AppPreferences
import com.gatekeeper.data.repository.ContactRepository
import com.gatekeeper.data.repository.EventRepository
import com.gatekeeper.data.repository.GateLogRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class GateNotificationService : NotificationListenerService() {

    @Inject lateinit var contactRepository: ContactRepository
    @Inject lateinit var eventRepository: EventRepository
    @Inject lateinit var appPreferences: AppPreferences
    @Inject lateinit var gateLogRepository: GateLogRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        private const val TAG = "GateNotificationSvc"
        private const val WHATSAPP_PACKAGE = "com.whatsapp"
        private const val WHATSAPP_BUSINESS_PACKAGE = "com.whatsapp.w4b"
        private const val FOREGROUND_CHANNEL_ID = "gate_service_channel"
        private const val FOREGROUND_NOTIFICATION_ID = 1001
        private const val ALERT_CHANNEL_ID = "gate_alert_channel"
        private const val ALERT_NOTIFICATION_ID = 1002
        private const val TRIGGER_COOLDOWN_MS = 30_000L
    }

    private val recentTriggers = mutableMapOf<String, Long>()

    override fun onCreate() {
        super.onCreate()
        startForegroundWithNotification()
        createAlertChannel()
        Log.i(TAG, "GateNotificationService started")
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        Log.i(TAG, "GateNotificationService stopped")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val pkg = sbn.packageName ?: return
        if (pkg != WHATSAPP_PACKAGE && pkg != WHATSAPP_BUSINESS_PACKAGE) return

        val extras = sbn.notification?.extras ?: return
        val senderName = extras.getString(Notification.EXTRA_TITLE) ?: return
        val messageText = extras.getString(Notification.EXTRA_TEXT) ?: return

        Log.d(TAG, "WhatsApp notification — sender: $senderName | message: $messageText")

        serviceScope.launch {
            processMessage(senderName, messageText)
        }
    }

    private suspend fun processMessage(senderName: String, message: String) {
        if (!appPreferences.monitoringEnabled.first()) {
            Log.d(TAG, "Monitoring disabled — ignoring message from $senderName")
            return
        }
        val keywords = appPreferences.triggerKeywords.first()

        if (!MessageMatcher.matches(message, keywords)) return

        val now = System.currentTimeMillis()

        // Check 1: permanent authorized contacts with daily timeframe
        val enabledContacts = contactRepository.getEnabledContacts()
        val matchedContact = enabledContacts.find { it.name.equals(senderName, ignoreCase = true) }
        if (matchedContact != null && isWithinAuthorizedTimeframe(matchedContact)) {
            triggerGate(matchedContact.phoneNumber, senderName, "Contacto permanente", now)
            return
        }

        // Check 2: contacts assigned to a currently active event
        val activeEvents = eventRepository.getActiveEventsWithContacts(now)
        val matchedEvent = activeEvents.find { ewc ->
            ewc.contacts.any { it.name.equals(senderName, ignoreCase = true) }
        }
        if (matchedEvent != null) {
            val phone = matchedEvent.contacts.first { it.name.equals(senderName, ignoreCase = true) }.phoneNumber
            triggerGate(phone, senderName, "Evento: ${matchedEvent.event.name}", now)
            return
        }

        Log.d(TAG, "$senderName is not authorized at this time")
    }

    private suspend fun triggerGate(phone: String, name: String, reason: String, now: Long) {
        val lastTrigger = recentTriggers[phone] ?: 0L
        if (now - lastTrigger < TRIGGER_COOLDOWN_MS) {
            Log.d(TAG, "Cooldown active for $name — ignoring duplicate trigger")
            return
        }
        recentTriggers[phone] = now
        Log.i(TAG, "Opening gate for $name ($reason)")
        gateLogRepository.logGateOpen(senderName = name, reason = reason)
        showAlertNotification(name)
        openGate()
    }

    private fun isWithinAuthorizedTimeframe(contact: AuthorizedContact): Boolean {
        val now = Calendar.getInstance()
        val dayOfWeek = now.get(Calendar.DAY_OF_WEEK)
        // Calendar.DAY_OF_WEEK: 1=Sunday, 2=Monday, ..., 7=Saturday
        // Our bitmask: bit 0 = Monday, ..., bit 5 = Saturday, bit 6 = Sunday
        val bitIndex = when (dayOfWeek) {
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> return false
        }
        val isDayAllowed = (contact.activeDays and (1 shl bitIndex)) != 0
        if (!isDayAllowed) return false

        val currentMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)
        return currentMinutes in contact.startTimeMinutes..contact.endTimeMinutes
    }

    private suspend fun openGate() {
        val gatePhone = appPreferences.gatePhoneNumber.first()
        if (gatePhone.isBlank()) {
            Log.w(TAG, "Gate phone number not configured — cannot call")
            return
        }
        withContext(Dispatchers.Main) {
            val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$gatePhone")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(callIntent)
            Log.i(TAG, "Calling gate: $gatePhone")
        }
    }

    private fun startForegroundWithNotification() {
        val manager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            FOREGROUND_CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_LOW
        ).apply { description = getString(R.string.notification_channel_desc) }
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, FOREGROUND_CHANNEL_ID)
            .setContentTitle(getString(R.string.service_notification_title))
            .setContentText(getString(R.string.service_notification_text))
            .setSmallIcon(R.drawable.ic_gate)
            .setOngoing(true)
            .build()

        startForeground(FOREGROUND_NOTIFICATION_ID, notification)
    }

    private fun createAlertChannel() {
        val channel = NotificationChannel(
            ALERT_CHANNEL_ID,
            getString(R.string.alert_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = getString(R.string.alert_channel_desc)
            enableVibration(true)
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun showAlertNotification(senderName: String) {
        val notification = NotificationCompat.Builder(this, ALERT_CHANNEL_ID)
            .setContentTitle(getString(R.string.alert_opening_title))
            .setContentText(getString(R.string.alert_opening_text, senderName))
            .setSmallIcon(R.drawable.ic_gate)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        getSystemService(NotificationManager::class.java)
            .notify(ALERT_NOTIFICATION_ID, notification)
    }
}
