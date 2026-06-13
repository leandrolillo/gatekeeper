package com.gatekeeper.data.model

import androidx.room.Entity

/**
 * Contacts authorized specifically for an event.
 * Stored independently of AuthorizedContact so one-time guests don't pollute the
 * permanent contacts list.
 */
@Entity(
    tableName = "event_contacts",
    primaryKeys = ["eventId", "phoneNumber"]
)
data class EventContact(
    val eventId: Long,
    val name: String,
    val phoneNumber: String
)
