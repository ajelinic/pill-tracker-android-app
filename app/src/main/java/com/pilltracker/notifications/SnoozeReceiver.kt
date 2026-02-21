package com.pilltracker.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SnoozeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != NotificationHelper.ACTION_SNOOZE) return

        NotificationHelper.cancelReminder(context)
        ReminderScheduler.scheduleSnooze(context, minutes = 15)
    }
}
