package com.pilltracker.data.repo

import com.pilltracker.data.db.DoseDao
import com.pilltracker.data.db.DoseEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

class DoseRepository(
    private val doseDao: DoseDao
) {
    fun observeTodayDose(): Flow<DoseEntity?> {
        val epochDay = LocalDate.now().toEpochDay()
        return doseDao.observeDoseForDay(epochDay)
    }

    suspend fun isTakenToday(): Boolean {
        val epochDay = LocalDate.now().toEpochDay()
        return doseDao.getDoseForDay(epochDay) != null
    }

    fun observeHistory(daysBack: Int): Flow<List<DoseEntity>> {
        val today = LocalDate.now()
        val from = today.minusDays(daysBack.toLong() - 1).toEpochDay()
        val to = today.toEpochDay()
        return doseDao.observeDosesBetween(from, to)
    }

    suspend fun markTakenNow() {
        val nowMillis = System.currentTimeMillis()
        val today = LocalDate.now(ZoneId.systemDefault())
        doseDao.upsert(
            DoseEntity(
                dateEpochDay = today.toEpochDay(),
                takenAtEpochMillis = nowMillis
            )
        )
    }
}
