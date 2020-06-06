package com.alancamargo.knockerupper.framework.local

import com.alancamargo.knockerupper.data.helpers.crashreport.CrashReportManager
import com.alancamargo.knockerupper.data.local.AlarmLocalDataSource
import com.alancamargo.knockerupper.domain.model.Alarm
import com.alancamargo.knockerupper.domain.model.QueryResult
import com.alancamargo.knockerupper.framework.local.db.AlarmDao
import com.alancamargo.knockerupper.framework.local.model.fromDatabaseToDomain
import com.alancamargo.knockerupper.framework.local.model.fromDomainToDatabase

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