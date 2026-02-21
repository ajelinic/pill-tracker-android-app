package com.pilltracker.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pilltracker.ui.vm.SettingsViewModel

@Composable
fun SettingsScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    vm: SettingsViewModel = viewModel()
) {
    val settings by vm.settings.collectAsState()
    val context = LocalContext.current

    var pillName by remember(settings.pillName) { mutableStateOf(settings.pillName) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = pillName,
            onValueChange = {
                pillName = it
                vm.setPillName(it)
            },
            label = { Text("Pill label") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Reminders")
            RowSwitch(
                checked = settings.remindersEnabled,
                onCheckedChange = { vm.setRemindersEnabled(it) },
                label = if (settings.remindersEnabled) "Enabled" else "Disabled"
            )

            Text("Daily reminder time: ${settings.reminderLabel}")
            Button(onClick = {
                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        vm.setReminderTime(hourOfDay, minute)
                    },
                    settings.reminderHour,
                    settings.reminderMinute,
                    true
                ).show()
            }) {
                Text("Change time")
            }
        }
    }
}

@Composable
private fun RowSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String
) {
    androidx.compose.foundation.layout.Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Switch(checked = checked, onCheckedChange = onCheckedChange)
        Text(label, modifier = Modifier.padding(top = 6.dp))
    }
}
