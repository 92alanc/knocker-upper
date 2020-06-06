package com.ukdev.smartbuzz.data.repository

import com.ukdev.smartbuzz.domain.model.Alarm
import com.ukdev.smartbuzz.domain.model.QueryResult

interface AlarmRepository {

    suspend fun getAlarms(): QueryResult<List<Alarm>>

    suspend fun saveOrUpdate(alarm: Alarm)

}