package com.alancamargo.knockerupper.data.repository

import com.alancamargo.knockerupper.data.local.AlarmLocalDataSource
import com.alancamargo.knockerupper.domain.model.Alarm
import com.alancamargo.knockerupper.domain.model.QueryResult
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AlarmRepositoryImplTest {

    @MockK lateinit var mockLocalDataSource: AlarmLocalDataSource

    private lateinit var repository: AlarmRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        repository = AlarmRepositoryImpl(mockLocalDataSource)
    }

    @Test
    fun shouldGetAlarmsFromLocalDataSource() = runBlockingTest {
        coEvery { mockLocalDataSource.getAlarms() } returns mockAlarms()

        val result = repository.getAlarms()

        assertThat(result).isInstanceOf(QueryResult.Success::class.java)
        require(result is QueryResult.Success)
        assertThat(result.body.size).isEqualTo(3)
    }

    @Test
    fun whenLocalDataSourceReturnsError_shouldReturnError() = runBlockingTest {
        coEvery { mockLocalDataSource.getAlarms() } returns QueryResult.Error

        val result = repository.getAlarms()

        assertThat(result).isInstanceOf(QueryResult.Error::class.java)
    }

    @Test
    fun shouldSaveOrUpdateAlarm() = runBlockingTest {
        repository.saveOrUpdate(mockk())

        coVerify { mockLocalDataSource.saveOrUpdate(any()) }
    }

    @Test
    fun shouldDeleteAlarm() = runBlockingTest {
        repository.delete(mockk())

        coVerify { mockLocalDataSource.delete(any()) }
    }

    private fun mockAlarms(): QueryResult<List<Alarm>> {
        val alarms = listOf<Alarm>(mockk(), mockk(), mockk())
        return QueryResult.Success(alarms)
    }

}