package com.alancamargo.knockerupper.ui.model

import com.alancamargo.knockerupper.domain.model.Alarm
import com.alancamargo.knockerupper.domain.model.Day
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UiTypeConversionsKtTest {

    @Test
    fun fromDomainToUi() {
        val domain = Alarm(
                id = "123-456",
                label = "Alarm",
                triggerTime = 123L,
                ringtone = null,
                frequency = listOf(Day.SATURDAY, Day.SUNDAY),
                vibrate = true,
                deleteOnDismiss = false,
                isActive = true,
                code = null
        )

        val expected = UiAlarm(
                id = "123-456",
                label = "Alarm",
                triggerTime = 123L,
                ringtone = null,
                frequency = listOf(Day.SATURDAY, Day.SUNDAY),
                vibrate = true,
                deleteOnDismiss = false,
                isActive = true,
                code = null
        )

        val actual = domain.fromDomainToUi()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun fromUiToDomain() {
        val ui = UiAlarm(
                id = "123-456",
                label = "Alarm",
                triggerTime = 123L,
                ringtone = null,
                frequency = listOf(Day.SATURDAY, Day.SUNDAY),
                vibrate = true,
                deleteOnDismiss = true,
                isActive = true,
                code = null
        )

        val expected = Alarm(
                id = "123-456",
                label = "Alarm",
                triggerTime = 123L,
                ringtone = null,
                frequency = listOf(Day.SATURDAY, Day.SUNDAY),
                vibrate = true,
                deleteOnDismiss = true,
                isActive = true,
                code = null
        )

        val actual = ui.fromUiToDomain()

        assertThat(actual).isEqualTo(expected)
    }

}