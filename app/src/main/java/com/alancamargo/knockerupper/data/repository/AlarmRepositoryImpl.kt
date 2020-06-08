package com.alancamargo.knockerupper.data.repository

import com.alancamargo.knockerupper.data.local.AlarmLocalDataSource
import com.alancamargo.knockerupper.domain.entities.Alarm
import com.alancamargo.knockerupper.domain.wrappers.QueryResult

class AlarmRepositoryImpl(private val localDataSource: AlarmLocalDataSource) : AlarmRepository {

    override suspend fun getAlarms(): QueryResult<List<Alarm>> {
        return localDataSource.getAlarms()
    }

    override suspend fun saveOrUpdate(alarm: Alarm) {
        localDataSource.saveOrUpdate(alarm)
    }

    override suspend fun delete(alarm: Alarm) {
        localDataSource.delete(alarm)
    }

}