package com.pilltracker.ui.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pilltracker.data.AppGraph
import com.pilltracker.data.db.DoseEntity
import com.pilltracker.data.settings.Settings
import com.pilltracker.notifications.ReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class HomeUiState(
    val pillName: String = "Blood pressure pill",
    val reminderLabel: String = "--:--",
    val remindersEnabled: Boolean = true,
    val takenToday: Boolean = false,
    val takenAtLabel: String? = null,
    val streakDays: Int = 0
)

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val settingsFlow = AppGraph.settingsRepository.settingsFlow
    private val todayDoseFlow = AppGraph.doseRepository.observeTodayDose()
    private val historyFlow = AppGraph.doseRepository.observeHistory(daysBack = 60)

    val uiState: StateFlow<HomeUiState> = combine(
        settingsFlow,
        todayDoseFlow,
        historyFlow
    ) { settings, todayDose, history ->
        HomeUiState(
            pillName = settings.pillName,
            reminderLabel = settings.reminderLabel,
            remindersEnabled = settings.remindersEnabled,
            takenToday = todayDose != null,
            takenAtLabel = todayDose?.let { formatTakenTime(it) },
            streakDays = computeStreak(history, todayDose != null)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    
fun markTakenNow() {
    viewModelScope.launch {
        AppGraph.doseRepository.markTakenNow()
        val settings = AppGraph.settingsRepository.settingsFlow.first()
        ReminderScheduler.scheduleTomorrowAtReminderTime(getApplication(), settings)
    }
}

    private fun formatTakenTime(entity: DoseEntity): String {
        val zone = ZoneId.systemDefault()
        val time = Instant.ofEpochMilli(entity.takenAtEpochMillis).atZone(zone).toLocalTime()
        return "%02d:%02d".format(time.hour, time.minute)
    }

    private fun computeStreak(history: List<DoseEntity>, takenToday: Boolean): Int {
        val zone = ZoneId.systemDefault()
        var date = LocalDate.now(zone)
        var streak = 0

        val takenSet = history.map { it.dateEpochDay }.toHashSet()

        if (!takenToday) {
            date = date.minusDays(1)
        }

        while (true) {
            val epochDay = date.toEpochDay()
            if (takenSet.contains(epochDay)) {
                streak++
                date = date.minusDays(1)
            } else {
                break
            }
        }

        return streak
    }
}
