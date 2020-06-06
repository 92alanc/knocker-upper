package com.ukdev.smartbuzz.data.repository

import com.ukdev.smartbuzz.data.local.AlarmLocalDataSource
import com.ukdev.smartbuzz.domain.model.Alarm
import com.ukdev.smartbuzz.domain.model.QueryResult

class AlarmRepositoryImpl(private val localDataSource: AlarmLocalDataSource) : AlarmRepository {

    override suspend fun getAlarms(): QueryResult<List<Alarm>> {
        return localDataSource.getAlarms()
    }

    override suspend fun saveOrUpdate(alarm: Alarm) {
        localDataSource.saveOrUpdate(alarm)
    }

}