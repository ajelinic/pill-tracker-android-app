package com.pilltracker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DoseDao {

    @Query("SELECT * FROM dose WHERE dateEpochDay = :epochDay LIMIT 1")
    fun observeDoseForDay(epochDay: Long): Flow<DoseEntity?>

    @Query("SELECT * FROM dose WHERE dateEpochDay = :epochDay LIMIT 1")
    suspend fun getDoseForDay(epochDay: Long): DoseEntity?

    @Query("SELECT * FROM dose WHERE dateEpochDay BETWEEN :fromEpochDay AND :toEpochDay ORDER BY dateEpochDay DESC")
    fun observeDosesBetween(fromEpochDay: Long, toEpochDay: Long): Flow<List<DoseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: DoseEntity)
}
