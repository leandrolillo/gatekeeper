package com.gatekeeper.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log

class BootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != "android.intent.action.QUICKBOOT_POWERON"
        ) return

        Log.i(TAG, "Boot completed — checking notification listener permission")

        // The NotificationListenerService re-binds automatically after boot if permission
        // was already granted. This receiver exists to log the event and can be extended
        // to show a reminder notification if the permission was revoked.
        val enabledListeners = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        ) ?: ""

        if (!enabledListeners.contains(context.packageName)) {
            Log.w(TAG, "Notification listener permission not granted after boot")
        } else {
            Log.i(TAG, "Notification listener is active after boot")
        }
    }
}
