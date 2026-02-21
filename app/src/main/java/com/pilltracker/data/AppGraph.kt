package com.pilltracker.data

import android.content.Context
import com.pilltracker.data.db.AppDatabase
import com.pilltracker.data.repo.DoseRepository
import com.pilltracker.data.settings.SettingsRepository

object AppGraph {
    lateinit var database: AppDatabase
        private set

    lateinit var doseRepository: DoseRepository
        private set

    lateinit var settingsRepository: SettingsRepository
        private set

    fun init(context: Context) {
        if (this::database.isInitialized) return

        val appContext = context.applicationContext
        database = AppDatabase.create(appContext)
        doseRepository = DoseRepository(database.doseDao())
        settingsRepository = SettingsRepository(appContext)
    }
}
