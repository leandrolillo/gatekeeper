package com.gatekeeper.data.db

import androidx.room.*
import com.gatekeeper.data.model.EventContact
import com.gatekeeper.data.model.EventWithContacts
import com.gatekeeper.data.model.GateEvent
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EventDao {

    @Transaction
    @Query("SELECT * FROM events ORDER BY startEpochMs ASC")
    abstract fun getAllEventsWithContacts(): Flow<List<EventWithContacts>>

    @Transaction
    @Query("SELECT * FROM events WHERE isEnabled = 1 AND startEpochMs <= :now AND endEpochMs >= :now")
    abstract suspend fun getActiveEventsWithContacts(now: Long): List<EventWithContacts>

    @Transaction
    @Query("SELECT * FROM events WHERE id = :id")
    abstract suspend fun getEventWithContacts(id: Long): EventWithContacts?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertEvent(event: GateEvent): Long

    @Update
    abstract suspend fun updateEvent(event: GateEvent)

    @Delete
    abstract suspend fun deleteEvent(event: GateEvent)

    @Query("DELETE FROM event_contacts WHERE eventId = :eventId")
    abstract suspend fun deleteEventContacts(eventId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertEventContacts(contacts: List<EventContact>)

    @Transaction
    open suspend fun upsertEventWithContacts(event: GateEvent, contacts: List<EventContact>): Long {
        val savedId = insertEvent(event)
        val actualId = if (event.id == 0L) savedId else event.id
        deleteEventContacts(actualId)
        insertEventContacts(contacts.map { it.copy(eventId = actualId) })
        return actualId
    }
}
