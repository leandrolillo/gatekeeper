package com.gatekeeper.data.repository

import com.gatekeeper.data.db.ContactDao
import com.gatekeeper.data.model.AuthorizedContact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(private val dao: ContactDao) {

    fun getAllContacts(): Flow<List<AuthorizedContact>> = dao.getAllContacts()

    suspend fun getEnabledContacts(): List<AuthorizedContact> = dao.getEnabledContacts()

    suspend fun getContactById(id: Long): AuthorizedContact? = dao.getContactById(id)

    suspend fun saveContact(contact: AuthorizedContact): Long = dao.insertContact(contact)

    suspend fun updateContact(contact: AuthorizedContact) = dao.updateContact(contact)

    suspend fun deleteContact(contact: AuthorizedContact) = dao.deleteContact(contact)
}
