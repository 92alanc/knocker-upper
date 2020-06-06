package com.alancamargo.knockerupper.framework.local.model

import com.alancamargo.knockerupper.domain.model.Alarm
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.DayOfWeek

class FrameworkTypeConversionsKtTest {

    @Test
    fun fromDomainToDatabase() {
        val domain = Alarm(
                id = "123-456",
                label = "Alarm",
                triggerTime = 123L,
                ringtone = "mock-ringtone",
                frequency = listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
                vibrate = true,
                deleteOnDismiss = false,
                isActive = true,
                code = null
        )

        val expected = DbAlarm(
                id = "123-456",
                label = "Alarm",
                triggerTime = 123L,
                ringtone = "mock-ringtone",
                frequency = "SATURDAY,SUNDAY",
                vibrate = true,
                deleteOnDismiss = false,
                isActive = true,
                code = null
        )

        val actual = domain.fromDomainToDatabase()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun fromDatabaseToDomain() {
        val database = DbAlarm(
                id = "123-456",
                label = "Alarm",
                triggerTime = 123L,
                ringtone = "mock-ringtone",
                frequency = "SATURDAY,SUNDAY",
                vibrate = false,
                deleteOnDismiss = true,
                isActive = true,
                code = null
        )

        val expected = Alarm(
                id = "123-456",
                label = "Alarm",
                triggerTime = 123L,
                ringtone = "mock-ringtone",
                frequency = listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
                vibrate = false,
                deleteOnDismiss = true,
                isActive = true,
                code = null
        )

        val actual = database.fromDatabaseToDomain()

        assertThat(actual).isEqualTo(expected)
    }

}