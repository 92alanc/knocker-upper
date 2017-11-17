package com.ukdev.smartbuzz.database

import android.support.test.runner.AndroidJUnit4
import com.ukdev.smartbuzz.common.BaseTest
import com.ukdev.smartbuzz.model.Alarm
import com.ukdev.smartbuzz.model.AlarmBuilder
import com.ukdev.smartbuzz.model.Time
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class AlarmDaoTest : BaseTest() {

    private lateinit var alarm: Alarm

    @Before
    fun init() {
        alarm = AlarmBuilder().setName("Test")
                .setRepetition(arrayOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY))
                .setVolume(4)
                .setTriggerTime(System.currentTimeMillis())
                .setVibrate(true)
                .setSleepCheckerOn(false)
                .build()
    }

    @After
    fun after() {
        clearDatabase()
    }

    @Test
    fun shouldInsert() {
        assertTrue(getDao().insert(alarm) > 0)
    }

    @Test
    fun shouldDelete() {
        val id = getDao().insert(alarm)
        alarm.id = id.toInt()
        assertTrue(getDao().delete(alarm))
    }

    @Test
    fun shouldUpdate() {
        val id = getDao().insert(alarm)
        alarm.id = id.toInt()
        alarm.name = "Test 2"
        alarm.triggerTime = System.currentTimeMillis() - Time.TEN_MINUTES
        alarm.snoozeDuration = Time.THIRTY_MINUTES
        alarm.repetition = arrayOf(Calendar.SATURDAY, Calendar.SUNDAY)
        alarm.isSleepCheckerOn = true
        assertTrue(getDao().update(alarm))
    }

    @Test
    fun shouldGetActiveAlarms() {
        alarm.isActive = false
        getDao().insert(alarm)
        alarm.isActive = true
        getDao().insert(alarm)
        assertTrue(getDao().activeAlarms.size == 2)
    }

    @Test
    fun shouldSelectAllAlarms() {
        getDao().insert(alarm)
        assertTrue(getDao().select().size == 2)
    }

    @Test
    fun shouldSelectAlarmById() {
        assertTrue(getDao().select(1) != null)
    }

}