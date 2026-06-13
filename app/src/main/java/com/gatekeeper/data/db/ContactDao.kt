package com.gatekeeper.data.db

import androidx.room.*
import com.gatekeeper.data.model.AuthorizedContact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Query("SELECT * FROM authorized_contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<AuthorizedContact>>

    @Query("SELECT * FROM authorized_contacts WHERE isEnabled = 1")
    suspend fun getEnabledContacts(): List<AuthorizedContact>

    @Query("SELECT * FROM authorized_contacts WHERE id = :id")
    suspend fun getContactById(id: Long): AuthorizedContact?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: AuthorizedContact): Long

    @Update
    suspend fun updateContact(contact: AuthorizedContact)

    @Delete
    suspend fun deleteContact(contact: AuthorizedContact)
}
