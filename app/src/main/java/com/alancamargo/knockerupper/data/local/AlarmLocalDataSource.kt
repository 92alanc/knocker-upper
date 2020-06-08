package com.alancamargo.knockerupper.data.local

import com.alancamargo.knockerupper.domain.entities.Alarm
import com.alancamargo.knockerupper.domain.wrappers.QueryResult

interface AlarmLocalDataSource {

    suspend fun getAlarms(): QueryResult<List<Alarm>>

    suspend fun saveOrUpdate(alarm: Alarm)

    suspend fun delete(alarm: Alarm)

}