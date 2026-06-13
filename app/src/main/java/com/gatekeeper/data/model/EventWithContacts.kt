package com.gatekeeper.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class EventWithContacts(
    @Embedded val event: GateEvent,
    @Relation(parentColumn = "id", entityColumn = "eventId")
    val contacts: List<EventContact>
)
