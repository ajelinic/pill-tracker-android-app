package com.pilltracker.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pilltracker.MainActivity

object NotificationHelper {

    private const val CHANNEL_ID = "pill_reminders"
    private const val CHANNEL_NAME = "Pill reminders"
    private const val NOTIFICATION_ID = 1001

    const val ACTION_TAKEN = "com.pilltracker.ACTION_TAKEN"
    const val ACTION_SNOOZE = "com.pilltracker.ACTION_SNOOZE"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < 26) return

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val existing = manager.getNotificationChannel(CHANNEL_ID)
        if (existing != null) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Daily reminders to take your pill"
        }

        manager.createNotificationChannel(channel)
    }

    fun showReminder(context: Context, pillName: String) {
        ensureChannel(context)

        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or pendingIntentImmutableFlag()
        )

        val takenIntent = PendingIntent.getBroadcast(
            context,
            1,
            Intent(context, DoseTakenReceiver::class.java).apply { action = ACTION_TAKEN },
            PendingIntent.FLAG_UPDATE_CURRENT or pendingIntentImmutableFlag()
        )

        val snoozeIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(context, SnoozeReceiver::class.java).apply { action = ACTION_SNOOZE },
            PendingIntent.FLAG_UPDATE_CURRENT or pendingIntentImmutableFlag()
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Time to take your pill")
            .setContentText(pillName)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(0, "Taken", takenIntent)
            .addAction(0, "Snooze 15 min", snoozeIntent)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    fun cancelReminder(context: Context) {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
    }

    private fun pendingIntentImmutableFlag(): Int {
        return if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0
    }
}
