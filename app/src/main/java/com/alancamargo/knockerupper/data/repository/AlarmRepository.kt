package com.alancamargo.knockerupper.data.repository

import com.alancamargo.knockerupper.domain.entities.Alarm
import com.alancamargo.knockerupper.domain.wrappers.QueryResult

interface AlarmRepository {

    suspend fun getAlarms(): QueryResult<List<Alarm>>

    suspend fun saveOrUpdate(alarm: Alarm)

    suspend fun delete(alarm: Alarm)

}