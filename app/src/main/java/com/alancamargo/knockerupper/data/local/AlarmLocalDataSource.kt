package com.alancamargo.knockerupper.data.local

import com.alancamargo.knockerupper.domain.model.Alarm
import com.alancamargo.knockerupper.domain.model.QueryResult

interface AlarmLocalDataSource {

    suspend fun getAlarms(): QueryResult<List<Alarm>>

    suspend fun saveOrUpdate(alarm: Alarm)

    suspend fun delete(alarm: Alarm)

}