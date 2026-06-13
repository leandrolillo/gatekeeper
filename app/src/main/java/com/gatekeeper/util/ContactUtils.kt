package com.gatekeeper.util

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

data class PhoneContact(val name: String, val phoneNumber: String)

fun readContactFromUri(context: Context, contactUri: Uri): PhoneContact? {
    val name = resolveDisplayName(context, contactUri) ?: return null
    val contactId = resolveContactId(context, contactUri) ?: return null
    val phone = resolvePhoneNumber(context, contactId) ?: return null
    return PhoneContact(name = name, phoneNumber = phone)
}

private fun resolveDisplayName(context: Context, contactUri: Uri): String? =
    context.contentResolver.query(
        contactUri,
        arrayOf(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY),
        null, null, null
    )?.use { cursor ->
        if (cursor.moveToFirst())
            cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
        else null
    }

private fun resolveContactId(context: Context, contactUri: Uri): String? =
    context.contentResolver.query(
        contactUri,
        arrayOf(ContactsContract.Contacts._ID),
        null, null, null
    )?.use { cursor ->
        if (cursor.moveToFirst())
            cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
        else null
    }

private fun resolvePhoneNumber(context: Context, contactId: String): String? =
    context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
        arrayOf(contactId),
        null
    )?.use { cursor ->
        if (cursor.moveToFirst())
            cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
        else null
    }
