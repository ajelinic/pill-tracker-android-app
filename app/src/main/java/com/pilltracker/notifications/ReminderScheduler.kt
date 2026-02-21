package com.pilltracker.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.pilltracker.data.settings.Settings
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    private const val UNIQUE_DAILY_WORK = "pill_daily_reminder"
    private const val UNIQUE_SNOOZE_WORK = "pill_snooze_reminder"

    fun cancelAll(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_DAILY_WORK)
        WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_SNOOZE_WORK)
    }

    fun scheduleNext(context: Context, settings: Settings) {
        if (!settings.remindersEnabled) {
            WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_DAILY_WORK)
            return
        }

        val delay = computeDelayToNextOccurrence(
            hour = settings.reminderHour,
            minute = settings.reminderMinute,
            forceTomorrow = false
        )

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            UNIQUE_DAILY_WORK,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun scheduleTomorrowAtReminderTime(context: Context, settings: Settings) {
        if (!settings.remindersEnabled) {
            WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_DAILY_WORK)
            return
        }

        val delay = computeDelayToNextOccurrence(
            hour = settings.reminderHour,
            minute = settings.reminderMinute,
            forceTomorrow = true
        )

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            UNIQUE_DAILY_WORK,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun scheduleSnooze(context: Context, minutes: Long = 15) {
        val request = OneTimeWorkRequestBuilder<SnoozeWorker>()
            .setInitialDelay(minutes, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            UNIQUE_SNOOZE_WORK,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    private fun computeDelayToNextOccurrence(hour: Int, minute: Int, forceTomorrow: Boolean): Long {
        val zone = ZoneId.systemDefault()
        val now = LocalDateTime.now(zone)

        val targetDate = if (forceTomorrow) {
            LocalDate.now(zone).plusDays(1)
        } else {
            LocalDate.now(zone)
        }

        var next = LocalDateTime.of(targetDate, LocalTime.of(hour, minute))
        if (!forceTomorrow && !next.isAfter(now)) {
            next = next.plusDays(1)
        }

        val duration = Duration.between(now, next)
        return duration.toMillis().coerceAtLeast(0)
    }
}
