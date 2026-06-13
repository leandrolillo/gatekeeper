package com.gatekeeper.data.db

import androidx.room.*
import com.gatekeeper.data.model.GateLog
import kotlinx.coroutines.flow.Flow

@Dao
interface GateLogDao {
    @Insert
    suspend fun insert(log: GateLog)

    @Query("SELECT * FROM gate_log ORDER BY timestampMs DESC")
    fun getAll(): Flow<List<GateLog>>

    @Query("DELETE FROM gate_log")
    suspend fun clearAll()
}
