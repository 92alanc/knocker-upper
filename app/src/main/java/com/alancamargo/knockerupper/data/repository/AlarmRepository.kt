package com.alancamargo.knockerupper.data.repository

import com.alancamargo.knockerupper.domain.model.Alarm
import com.alancamargo.knockerupper.domain.model.QueryResult

interface AlarmRepository {

    suspend fun getAlarms(): QueryResult<List<Alarm>>

    suspend fun saveOrUpdate(alarm: Alarm)

    suspend fun delete(alarm: Alarm)

}