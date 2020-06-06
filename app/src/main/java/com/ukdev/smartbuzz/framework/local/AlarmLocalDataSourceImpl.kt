package com.ukdev.smartbuzz.framework.local

import com.ukdev.smartbuzz.data.local.AlarmLocalDataSource
import com.ukdev.smartbuzz.domain.model.Alarm
import com.ukdev.smartbuzz.domain.model.QueryResult
import com.ukdev.smartbuzz.framework.local.db.AlarmDao
import com.ukdev.smartbuzz.framework.local.model.fromDatabaseToDomain

class AlarmLocalDataSourceImpl(private val alarmDao: AlarmDao) : AlarmLocalDataSource {

    override suspend fun getAlarms(): QueryResult<List<Alarm>> {
        return try {
            val alarms = alarmDao.select().map {
                it.fromDatabaseToDomain()
            }
            QueryResult.Success(alarms)
        } catch (t: Throwable) {
            QueryResult.Error
        }
    }

}