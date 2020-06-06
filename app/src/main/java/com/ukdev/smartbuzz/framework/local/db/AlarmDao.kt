package com.ukdev.smartbuzz.framework.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ukdev.smartbuzz.framework.local.model.DbAlarm

@Dao
interface AlarmDao {

    @Query("SELECT * FROM DbAlarm ORDER BY triggerTime ASC")
    suspend fun select(): List<DbAlarm>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(alarm: DbAlarm)

    @Query("DELETE FROM DbAlarm WHERE id = :id")
    suspend fun delete(id: String)

}