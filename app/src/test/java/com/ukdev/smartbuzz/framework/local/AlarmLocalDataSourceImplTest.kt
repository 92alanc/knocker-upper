package com.ukdev.smartbuzz.framework.local

import com.google.common.truth.Truth.assertThat
import com.ukdev.smartbuzz.domain.model.QueryResult
import com.ukdev.smartbuzz.framework.local.db.AlarmDao
import com.ukdev.smartbuzz.framework.local.model.DbAlarm
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class AlarmLocalDataSourceImplTest {

    @MockK
    lateinit var mockAlarmDao: AlarmDao

    private lateinit var localDataSource: AlarmLocalDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        localDataSource = AlarmLocalDataSourceImpl(mockAlarmDao)
    }

    @Test
    fun shouldGetAlarmsFromDatabase() = runBlockingTest {
        coEvery { mockAlarmDao.select() } returns mockAlarms()

        val result = localDataSource.getAlarms()

        assertThat(result).isInstanceOf(QueryResult.Success::class.java)
        require(result is QueryResult.Success)
        assertThat(result.body.size).isEqualTo(2)
    }

    @Test
    fun whenDbThrowsException_shouldReturnError() = runBlockingTest {
        coEvery { mockAlarmDao.select() } throws IOException()

        val result = localDataSource.getAlarms()

        assertThat(result).isInstanceOf(QueryResult.Error::class.java)
    }

    @Test
    fun shouldSaveOrUpdateAlarm() = runBlockingTest {
        localDataSource.saveOrUpdate(mockk(relaxed = true))

        coVerify { mockAlarmDao.insertOrUpdate(any()) }
    }

    @Test
    fun shouldDeleteAlarm() = runBlockingTest {
        localDataSource.delete(mockk(relaxed = true))

        coVerify { mockAlarmDao.delete(any()) }
    }

    private fun mockAlarms(): List<DbAlarm> {
        val alarm1 = DbAlarm(
                "123-456",
                "Alarm 1",
                123L,
                "MONDAY,TUESDAY",
                true,
                "code"
        )

        val alarm2 = DbAlarm(
                "789-012",
                "Alarm 2",
                456L,
                "SATURDAY,SUNDAY",
                false,
                code = null
        )

        return listOf(alarm1, alarm2)
    }

}