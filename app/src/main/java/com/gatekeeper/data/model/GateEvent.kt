package com.gatekeeper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class GateEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    /** Unix epoch ms */
    val startEpochMs: Long,
    /** Unix epoch ms */
    val endEpochMs: Long,
    val isEnabled: Boolean = true
)
