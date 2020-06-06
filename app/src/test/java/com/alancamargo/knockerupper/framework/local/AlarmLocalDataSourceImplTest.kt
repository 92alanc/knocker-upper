package com.alancamargo.knockerupper.framework.local

import com.alancamargo.knockerupper.data.helpers.crashreport.CrashReportManager
import com.alancamargo.knockerupper.domain.model.QueryResult
import com.alancamargo.knockerupper.framework.local.db.AlarmDao
import com.alancamargo.knockerupper.framework.local.model.DbAlarm
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.IOException

class AlarmLocalDataSourceImplTest {

    @MockK lateinit var mockAlarmDao: AlarmDao
    @MockK lateinit var mockCrashReportManager: CrashReportManager

    private lateinit var localDataSource: AlarmLocalDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        localDataSource = AlarmLocalDataSourceImpl(mockAlarmDao, mockCrashReportManager)
    }

    @Test
    fun shouldGetAlarmsFromDatabase() = runBlocking {
        coEvery { mockAlarmDao.select() } returns mockAlarms()

        val result = localDataSource.getAlarms()

        assertThat(result).isInstanceOf(QueryResult.Success::class.java)
        require(result is QueryResult.Success)
        assertThat(result.body.size).isEqualTo(2)
    }

    @Test
    fun whenDbThrowsException_shouldReturnError() = runBlocking {
        coEvery { mockAlarmDao.select() } throws IOException()

        val result = localDataSource.getAlarms()

        assertThat(result).isInstanceOf(QueryResult.Error::class.java)
    }

    @Test
    fun shouldSaveOrUpdateAlarm() = runBlocking {
        localDataSource.saveOrUpdate(mockk(relaxed = true))

        coVerify { mockAlarmDao.insertOrUpdate(any()) }
    }

    @Test
    fun shouldDeleteAlarm() = runBlocking {
        localDataSource.delete(mockk(relaxed = true))

        coVerify { mockAlarmDao.delete(any()) }
    }

    @Test
    fun errorGettingAlarms_shouldLogToCrashReport() = runBlocking {
        coEvery { mockAlarmDao.select() } throws IOException()

        localDataSource.getAlarms()

        verify { mockCrashReportManager.log(any<Throwable>()) }
    }

    @Test
    fun errorSavingOrUpdating_shouldLogToCrashReport() = runBlocking {
        coEvery { mockAlarmDao.insertOrUpdate(any()) } throws IOException()

        localDataSource.saveOrUpdate(mockk())

        verify { mockCrashReportManager.log(any<Throwable>()) }
    }

    @Test
    fun errorDeleting_shouldLogToCrashReport() = runBlocking {
        coEvery { mockAlarmDao.delete(any()) } throws IOException()

        localDataSource.delete(mockk())

        verify { mockCrashReportManager.log(any<Throwable>()) }
    }

    private fun mockAlarms(): List<DbAlarm> {
        val alarm1 = DbAlarm(
                "123-456",
                "Alarm 1",
                123L,
                "mock-ringtone",
                "MONDAY,TUESDAY",
                vibrate = true,
                deleteOnDismiss = false,
                isActive = true,
                code = "code"
        )

        val alarm2 = DbAlarm(
                "789-012",
                "Alarm 2",
                456L,
                "mock-ringtone",
                "SATURDAY,SUNDAY",
                vibrate = false,
                deleteOnDismiss = true,
                isActive = true,
                code = null
        )

        return listOf(alarm1, alarm2)
    }

}