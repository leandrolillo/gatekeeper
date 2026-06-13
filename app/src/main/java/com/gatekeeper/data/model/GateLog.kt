package com.gatekeeper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gate_log")
data class GateLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestampMs: Long,
    val senderName: String,
    val reason: String
)
