package com.pilltracker.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pilltracker.data.AppGraph
import kotlinx.coroutines.flow.first

class SnoozeWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        AppGraph.init(applicationContext)
        val settings = AppGraph.settingsRepository.settingsFlow.first()

        if (settings.remindersEnabled) {
            // Snooze reminder: show a reminder again, but don't touch the daily schedule.
            NotificationHelper.showReminder(applicationContext, settings.pillName)
        }

        return Result.success()
    }
}
