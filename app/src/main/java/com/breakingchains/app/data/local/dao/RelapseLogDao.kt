package com.breakingchains.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breakingchains.app.data.local.entity.RelapseLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RelapseLogDao {
    @Query("SELECT * FROM relapse_logs WHERE userId = :userId ORDER BY timestamp DESC")
    fun getLogsForUser(userId: String): Flow<List<RelapseLogEntity>>

    @Query("SELECT * FROM relapse_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<RelapseLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: RelapseLogEntity)

    @Delete
    suspend fun deleteLog(log: RelapseLogEntity)
}
