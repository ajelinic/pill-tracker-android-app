package com.pilltracker.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pilltracker.ui.vm.HistoryViewModel

@Composable
fun HistoryScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    vm: HistoryViewModel = viewModel()
) {
    val items by vm.items.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text("History (last 60 days)", style = MaterialTheme.typography.headlineSmall)
        }

        items(items) { it ->
            Text(
                text = buildString {
                    append(it.dateLabel)
                    append(" • ")
                    append(if (it.taken) "Taken" else "Not taken")
                    if (it.takenAtLabel != null) append(" at ${it.takenAtLabel}")
                },
                modifier = Modifier.padding(vertical = 10.dp)
            )
            Divider()
        }
    }
}
