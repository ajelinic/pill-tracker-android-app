package com.pilltracker.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pilltracker.data.AppGraph
import kotlinx.coroutines.flow.first

class ReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        AppGraph.init(applicationContext)

        val settings = AppGraph.settingsRepository.settingsFlow.first()
        if (!settings.remindersEnabled) return Result.success()

        val alreadyTaken = AppGraph.doseRepository.isTakenToday()
        if (!alreadyTaken) {
            NotificationHelper.showReminder(applicationContext, settings.pillName)
        }

        // Always schedule the next occurrence.
        ReminderScheduler.scheduleNext(applicationContext, settings)
        return Result.success()
    }
}
