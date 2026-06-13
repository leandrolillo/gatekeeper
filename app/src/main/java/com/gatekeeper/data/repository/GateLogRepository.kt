package com.gatekeeper.data.repository

import com.gatekeeper.data.db.GateLogDao
import com.gatekeeper.data.model.GateLog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GateLogRepository @Inject constructor(private val dao: GateLogDao) {

    val allLogs: Flow<List<GateLog>> = dao.getAll()

    suspend fun logGateOpen(senderName: String, reason: String) {
        dao.insert(GateLog(
            timestampMs = System.currentTimeMillis(),
            senderName = senderName,
            reason = reason
        ))
    }

    suspend fun clearAll() = dao.clearAll()
}
