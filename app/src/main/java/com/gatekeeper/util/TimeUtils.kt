package com.gatekeeper.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatTime(totalMinutes: Int): String {
    val h = totalMinutes / 60
    val m = totalMinutes % 60
    return "%02d:%02d".format(h, m)
}

private val EVENT_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy  HH:mm")

fun formatEventDateTime(epochMs: Long): String =
    Instant.ofEpochMilli(epochMs)
        .atZone(ZoneId.systemDefault())
        .format(EVENT_FORMATTER)

private val DAY_NAMES = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

fun dayNames(bitmask: Int): String {
    if (bitmask == 0b1111111) return "Every day"
    val selected = DAY_NAMES.filterIndexed { index, _ -> (bitmask and (1 shl index)) != 0 }
    return selected.joinToString(", ")
}
