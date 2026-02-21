package com.pilltracker.ui.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pilltracker.data.AppGraph
import com.pilltracker.data.settings.Settings
import com.pilltracker.notifications.ReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    val settings: StateFlow<Settings> =
        AppGraph.settingsRepository.settingsFlow
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                Settings("Blood pressure pill", 8, 0, true)
            )

    fun setPillName(name: String) {
        viewModelScope.launch {
            AppGraph.settingsRepository.setPillName(name)
        }
    }

    fun setRemindersEnabled(enabled: Boolean) {
        viewModelScope.launch {
            AppGraph.settingsRepository.setRemindersEnabled(enabled)
            ReminderScheduler.scheduleNext(getApplication(), settings.value.copy(remindersEnabled = enabled))
        }
    }

    fun setReminderTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            AppGraph.settingsRepository.setReminderTime(hour, minute)
            ReminderScheduler.scheduleNext(getApplication(), settings.value.copy(reminderHour = hour, reminderMinute = minute))
        }
    }
}
