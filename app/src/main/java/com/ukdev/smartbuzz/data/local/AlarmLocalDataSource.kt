package com.ukdev.smartbuzz.data.local

import com.ukdev.smartbuzz.domain.model.Alarm
import com.ukdev.smartbuzz.domain.model.QueryResult

interface AlarmLocalDataSource {

    suspend fun getAlarms(): QueryResult<List<Alarm>>

}