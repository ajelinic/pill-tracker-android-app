package com.pilltracker.ui.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pilltracker.data.AppGraph
import com.pilltracker.data.db.DoseEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class HistoryItem(
    val dateLabel: String,
    val taken: Boolean,
    val takenAtLabel: String? = null
)

class HistoryViewModel(app: Application) : AndroidViewModel(app) {

    private val daysBack = 60

    val items: StateFlow<List<HistoryItem>> =
        AppGraph.doseRepository.observeHistory(daysBack)
            .map { doses -> toHistoryItems(doses) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private fun toHistoryItems(doses: List<DoseEntity>): List<HistoryItem> {
        val zone = ZoneId.systemDefault()
        val doseMap = doses.associateBy { it.dateEpochDay }

        val today = LocalDate.now(zone)
        val result = ArrayList<HistoryItem>(daysBack)

        for (i in 0 until daysBack) {
            val day = today.minusDays(i.toLong())
            val epochDay = day.toEpochDay()
            val dose = doseMap[epochDay]
            result += HistoryItem(
                dateLabel = day.toString(),
                taken = dose != null,
                takenAtLabel = dose?.let { formatTakenTime(it) }
            )
        }

        return result
    }

    private fun formatTakenTime(entity: DoseEntity): String {
        val zone = ZoneId.systemDefault()
        val time = Instant.ofEpochMilli(entity.takenAtEpochMillis).atZone(zone).toLocalTime()
        return "%02d:%02d".format(time.hour, time.minute)
    }
}
