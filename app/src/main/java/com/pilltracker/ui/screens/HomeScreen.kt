package com.pilltracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pilltracker.ui.vm.HomeViewModel

@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    vm: HomeViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = state.pillName,
            style = MaterialTheme.typography.headlineSmall
        )

        Card(Modifier.padding(top = 4.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Reminder: ${state.reminderLabel} ${if (state.remindersEnabled) "" else "(disabled)"}")
                Text(
                    text = if (state.takenToday) {
                        "Today: Taken at ${state.takenAtLabel}"
                    } else {
                        "Today: Not taken yet"
                    }
                )
                Text("Streak: ${state.streakDays} day(s)")
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { vm.markTakenNow() },
            enabled = !state.takenToday,
        ) {
            Text(if (state.takenToday) "Already taken today" else "Mark as taken")
        }
    }
}
