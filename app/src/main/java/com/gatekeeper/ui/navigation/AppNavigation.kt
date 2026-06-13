package com.gatekeeper.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.gatekeeper.R
import com.gatekeeper.ui.screens.AddEditContactScreen
import com.gatekeeper.ui.screens.AddEditEventScreen
import com.gatekeeper.ui.screens.ContactsScreen
import com.gatekeeper.ui.screens.EventsScreen
import com.gatekeeper.ui.screens.LogScreen
import com.gatekeeper.ui.screens.SettingsScreen
import com.gatekeeper.ui.viewmodel.MainViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Main.route) {

        composable(Screen.Main.route) {
            MainScreen(
                onAddContact = { navController.navigate(Screen.AddContact.route) },
                onEditContact = { id -> navController.navigate(Screen.EditContact.buildRoute(id)) },
                onAddEvent = { navController.navigate(Screen.AddEvent.route) },
                onEditEvent = { id -> navController.navigate(Screen.EditEvent.buildRoute(id)) },
                onSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.AddContact.route) {
            AddEditContactScreen(contactId = 0L, onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.EditContact.route,
            arguments = listOf(navArgument("contactId") { type = NavType.LongType })
        ) { back ->
            AddEditContactScreen(
                contactId = back.arguments?.getLong("contactId") ?: 0L,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.AddEvent.route) {
            AddEditEventScreen(eventId = 0L, onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.EditEvent.route,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { back ->
            AddEditEventScreen(
                eventId = back.arguments?.getLong("eventId") ?: 0L,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    onAddContact: () -> Unit,
    onEditContact: (Long) -> Unit,
    onAddEvent: () -> Unit,
    onEditEvent: (Long) -> Unit,
    onSettings: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = state.selectedTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    icon = { Icon(Icons.Default.People, contentDescription = null) },
                    label = { Text(stringResource(R.string.tab_contacts)) }
                )
                NavigationBarItem(
                    selected = state.selectedTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    icon = { Icon(Icons.Default.Event, contentDescription = null) },
                    label = { Text(stringResource(R.string.tab_events)) }
                )
                NavigationBarItem(
                    selected = state.selectedTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    icon = { Icon(Icons.Default.History, contentDescription = null) },
                    label = { Text(stringResource(R.string.tab_log)) }
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OpenGateButton(
                contactName = state.gateContactName,
                phoneNumber = state.gatePhoneNumber,
                onOpen = { viewModel.openGate(context) },
                onConfigure = onSettings
            )
            when (state.selectedTab) {
                0 -> ContactsScreen(
                    onAddContact = onAddContact,
                    onEditContact = onEditContact
                )
                1 -> EventsScreen(
                    onAddEvent = onAddEvent,
                    onEditEvent = onEditEvent
                )
                2 -> LogScreen()
            }
        }
    }
}

@Composable
private fun OpenGateButton(
    contactName: String,
    phoneNumber: String,
    onOpen: () -> Unit,
    onConfigure: () -> Unit
) {
    val configured = phoneNumber.isNotBlank()
    ElevatedCard(
        onClick = if (configured) onOpen else onConfigure,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (configured) MaterialTheme.colorScheme.primaryContainer
                             else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = if (configured) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.open_gate),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (configured) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (configured) contactName.ifBlank { phoneNumber }
                           else stringResource(R.string.gate_not_configured),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (configured) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}
