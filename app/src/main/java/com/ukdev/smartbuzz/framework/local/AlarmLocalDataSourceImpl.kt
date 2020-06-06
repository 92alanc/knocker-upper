package com.ukdev.smartbuzz.framework.local

import com.ukdev.smartbuzz.data.helpers.crashreport.CrashReportManager
import com.ukdev.smartbuzz.data.local.AlarmLocalDataSource
import com.ukdev.smartbuzz.domain.model.Alarm
import com.ukdev.smartbuzz.domain.model.QueryResult
import com.ukdev.smartbuzz.framework.local.db.AlarmDao
import com.ukdev.smartbuzz.framework.local.model.fromDatabaseToDomain
import com.ukdev.smartbuzz.framework.local.model.fromDomainToDatabase

class AlarmLocalDataSourceImpl(
        private val alarmDao: AlarmDao,
        private val crashReportManager: CrashReportManager
) : AlarmLocalDataSource {

    override suspend fun getAlarms(): QueryResult<List<Alarm>> {
        return try {
            val alarms = alarmDao.select().map {
                it.fromDatabaseToDomain()
            }
            QueryResult.Success(alarms)
        } catch (t: Throwable) {
            crashReportManager.log(t)
            QueryResult.Error
        }
    }

    override suspend fun saveOrUpdate(alarm: Alarm) {
        try {
            alarmDao.insertOrUpdate(alarm.fromDomainToDatabase())
        } catch (t: Throwable) {
            crashReportManager.log(t)
        }
    }

    override suspend fun delete(alarm: Alarm) {
        try {
            alarmDao.delete(alarm.id)
        } catch (t: Throwable) {
            crashReportManager.log(t)
        }
    }

}