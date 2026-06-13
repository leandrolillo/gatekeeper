package com.gatekeeper.ui.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object AddContact : Screen("contacts/add")
    data object EditContact : Screen("contacts/edit/{contactId}") {
        fun buildRoute(contactId: Long) = "contacts/edit/$contactId"
    }
    data object AddEvent : Screen("events/add")
    data object EditEvent : Screen("events/edit/{eventId}") {
        fun buildRoute(eventId: Long) = "events/edit/$eventId"
    }
    data object Settings : Screen("settings")
}
