package com.gatekeeper.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.gatekeeper.R
import com.gatekeeper.ui.viewmodel.AddEditContactViewModel
import com.gatekeeper.util.formatTime
import com.gatekeeper.util.readContactFromUri

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddEditContactScreen(
    contactId: Long,
    onNavigateBack: () -> Unit,
    viewModel: AddEditContactViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onNavigateBack()
    }

    val readContactsPermission = rememberPermissionState(android.Manifest.permission.READ_CONTACTS)

    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        val contact = readContactFromUri(context, uri) ?: return@rememberLauncherForActivityResult
        viewModel.onNameChange(contact.name)
        viewModel.onPhoneChange(contact.phoneNumber)
    }

    val isEditing = contactId > 0L
    val title = if (isEditing) stringResource(R.string.edit_contact)
    else stringResource(R.string.add_contact)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Contact picker card — shown always; in edit mode it allows re-picking
            ContactPickerCard(
                selectedName = state.name,
                selectedPhone = state.phoneNumber,
                onPickContact = {
                    if (readContactsPermission.status.isGranted) {
                        contactPickerLauncher.launch(null)
                    } else {
                        readContactsPermission.launchPermissionRequest()
                    }
                }
            )

            // After permission is granted (re-composition), auto-launch picker if nothing selected
            LaunchedEffect(readContactsPermission.status.isGranted) {
                if (readContactsPermission.status.isGranted && state.name.isBlank() && !isEditing) {
                    contactPickerLauncher.launch(null)
                }
            }

            DaysSelector(
                activeDays = state.activeDays,
                onActiveDaysChange = viewModel::onActiveDaysChange
            )

            TimeRangePicker(
                startMinutes = state.startTimeMinutes,
                endMinutes = state.endTimeMinutes,
                onStartChange = viewModel::onStartTimeChange,
                onEndChange = viewModel::onEndTimeChange
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.enabled),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(checked = state.isEnabled, onCheckedChange = viewModel::onEnabledChange)
            }

            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.name.isNotBlank() && state.phoneNumber.isNotBlank()
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
private fun ContactPickerCard(
    selectedName: String,
    selectedPhone: String,
    onPickContact: () -> Unit
) {
    val hasContact = selectedName.isNotBlank()
    OutlinedCard(
        onClick = onPickContact,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = if (hasContact) Icons.Default.Person else Icons.Default.ContactPhone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                if (hasContact) {
                    Text(text = selectedName, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = selectedPhone,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = stringResource(R.string.pick_contact),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.pick_contact_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (hasContact) {
                TextButton(onClick = onPickContact) {
                    Text(stringResource(R.string.change))
                }
            }
        }
    }
}

@Composable
private fun DaysSelector(activeDays: Int, onActiveDaysChange: (Int) -> Unit) {
    val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
    val dayFullNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Column {
        Text(
            text = stringResource(R.string.active_days),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            dayLabels.forEachIndexed { index, label ->
                val isSelected = (activeDays and (1 shl index)) != 0
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        val newDays = if (isSelected)
                            activeDays and (1 shl index).inv()
                        else
                            activeDays or (1 shl index)
                        onActiveDaysChange(newDays)
                    },
                    label = { Text(dayFullNames[index]) }
                )
            }
        }
    }
}

@Composable
private fun TimeRangePicker(
    startMinutes: Int,
    endMinutes: Int,
    onStartChange: (Int) -> Unit,
    onEndChange: (Int) -> Unit
) {
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    Column {
        Text(
            text = stringResource(R.string.authorized_timeframe),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = { showStartPicker = true },
                modifier = Modifier.weight(1f)
            ) {
                Text("${stringResource(R.string.from)}: ${formatTime(startMinutes)}")
            }
            OutlinedButton(
                onClick = { showEndPicker = true },
                modifier = Modifier.weight(1f)
            ) {
                Text("${stringResource(R.string.to)}: ${formatTime(endMinutes)}")
            }
        }
    }

    if (showStartPicker) {
        TimePickerDialog(
            initialMinutes = startMinutes,
            onDismiss = { showStartPicker = false },
            onConfirm = { minutes ->
                onStartChange(minutes)
                showStartPicker = false
            }
        )
    }

    if (showEndPicker) {
        TimePickerDialog(
            initialMinutes = endMinutes,
            onDismiss = { showEndPicker = false },
            onConfirm = { minutes ->
                onEndChange(minutes)
                showEndPicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    initialMinutes: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = initialMinutes / 60,
        initialMinute = initialMinutes % 60,
        is24Hour = true
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_time)) },
        text = { TimePicker(state = state) },
        confirmButton = {
            TextButton(onClick = { onConfirm(state.hour * 60 + state.minute) }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}
