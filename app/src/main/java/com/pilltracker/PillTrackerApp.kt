package com.pilltracker

import android.app.Application
import com.pilltracker.data.AppGraph
import com.pilltracker.notifications.ReminderScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PillTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppGraph.init(this)

        // Schedule next reminder on app start (best-effort).
        CoroutineScope(Dispatchers.Default).launch {
            val settings = AppGraph.settingsRepository.settingsFlow.first()
            ReminderScheduler.scheduleNext(applicationContext, settings)
        }
    }
}
