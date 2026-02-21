package com.pilltracker.data.settings

data class Settings(
    val pillName: String,
    val reminderHour: Int,
    val reminderMinute: Int,
    val remindersEnabled: Boolean
) {
    val reminderLabel: String get() = "%02d:%02d".format(reminderHour, reminderMinute)
}
