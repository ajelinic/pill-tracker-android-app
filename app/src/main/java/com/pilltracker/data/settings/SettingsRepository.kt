package com.pilltracker.data.settings

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(context: Context) {

    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("pill_tracker_settings") }
    )

    private object Keys {
        val pillName = stringPreferencesKey("pill_name")
        val reminderHour = intPreferencesKey("reminder_hour")
        val reminderMinute = intPreferencesKey("reminder_minute")
        val remindersEnabled = booleanPreferencesKey("reminders_enabled")
    }

    val settingsFlow: Flow<Settings> = dataStore.data.map { prefs ->
        val name = prefs[Keys.pillName] ?: "Blood pressure pill"
        val hour = prefs[Keys.reminderHour] ?: 8
        val minute = prefs[Keys.reminderMinute] ?: 0
        val enabled = prefs[Keys.remindersEnabled] ?: true

        Settings(
            pillName = name,
            reminderHour = hour,
            reminderMinute = minute,
            remindersEnabled = enabled
        )
    }

    suspend fun setPillName(name: String) {
        dataStore.edit { it[Keys.pillName] = name }
    }

    suspend fun setReminderTime(hour: Int, minute: Int) {
        dataStore.edit {
            it[Keys.reminderHour] = hour
            it[Keys.reminderMinute] = minute
        }
    }

    suspend fun setRemindersEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.remindersEnabled] = enabled }
    }
}
