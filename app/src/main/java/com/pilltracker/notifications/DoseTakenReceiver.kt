package com.pilltracker.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pilltracker.data.AppGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DoseTakenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != NotificationHelper.ACTION_TAKEN) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                AppGraph.init(context.applicationContext)
                AppGraph.doseRepository.markTakenNow()
                NotificationHelper.cancelReminder(context)

                val settings = AppGraph.settingsRepository.settingsFlow.first()
                ReminderScheduler.scheduleTomorrowAtReminderTime(context, settings)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
