package com.gatekeeper.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gatekeeper.data.model.AuthorizedContact
import com.gatekeeper.data.model.EventContact
import com.gatekeeper.data.model.GateEvent
import com.gatekeeper.data.model.GateLog

@Database(
    entities = [AuthorizedContact::class, GateEvent::class, EventContact::class, GateLog::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun eventDao(): EventDao
    abstract fun gateLogDao(): GateLogDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS events (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        startEpochMs INTEGER NOT NULL,
                        endEpochMs INTEGER NOT NULL,
                        isEnabled INTEGER NOT NULL DEFAULT 1
                    )
                """.trimIndent())
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS event_contacts (
                        eventId INTEGER NOT NULL,
                        name TEXT NOT NULL,
                        phoneNumber TEXT NOT NULL,
                        PRIMARY KEY (eventId, phoneNumber),
                        FOREIGN KEY (eventId) REFERENCES events(id) ON DELETE CASCADE
                    )
                """.trimIndent())
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS gate_log (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        timestampMs INTEGER NOT NULL,
                        senderName TEXT NOT NULL,
                        reason TEXT NOT NULL
                    )
                """.trimIndent())
            }
        }
    }
}