package com.gatekeeper.data.repository

import com.gatekeeper.data.db.EventDao
import com.gatekeeper.data.model.EventContact
import com.gatekeeper.data.model.EventWithContacts
import com.gatekeeper.data.model.GateEvent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(private val dao: EventDao) {

    fun getAllEventsWithContacts(): Flow<List<EventWithContacts>> =
        dao.getAllEventsWithContacts()

    suspend fun getActiveEventsWithContacts(now: Long = System.currentTimeMillis()): List<EventWithContacts> =
        dao.getActiveEventsWithContacts(now)

    suspend fun getEventWithContacts(id: Long): EventWithContacts? =
        dao.getEventWithContacts(id)

    suspend fun saveEventWithContacts(event: GateEvent, contacts: List<EventContact>): Long =
        dao.upsertEventWithContacts(event, contacts)

    suspend fun updateEvent(event: GateEvent) = dao.updateEvent(event)

    suspend fun deleteEvent(event: GateEvent) = dao.deleteEvent(event)
}
