package com.pilltracker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dose")
data class DoseEntity(
    @PrimaryKey val dateEpochDay: Long,
    val takenAtEpochMillis: Long
)
