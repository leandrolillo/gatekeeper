package com.gatekeeper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authorized_contacts")
data class AuthorizedContact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phoneNumber: String,
    /** Days bitmask: bit 0 = Monday … bit 6 = Sunday */
    val activeDays: Int = 0b1111111,
    /** Minutes from midnight, e.g. 08:00 → 480 */
    val startTimeMinutes: Int = 0,
    /** Minutes from midnight, e.g. 22:00 → 1320 */
    val endTimeMinutes: Int = 1439,
    val isEnabled: Boolean = true
)
