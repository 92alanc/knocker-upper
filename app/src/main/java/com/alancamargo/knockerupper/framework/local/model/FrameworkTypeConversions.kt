package com.alancamargo.knockerupper.framework.local.model

import com.alancamargo.knockerupper.domain.model.Alarm
import com.alancamargo.knockerupper.domain.model.Day

private const val STRING_SEPARATOR = ","

fun Alarm.fromDomainToDatabase(): DbAlarm {
    val frequency = this.frequency.joinToString(STRING_SEPARATOR)

    return DbAlarm(
            id,
            label,
            triggerTime,
            ringtone,
            frequency,
            vibrate,
            deleteOnDismiss,
            isActive,
            code
    )
}

fun DbAlarm.fromDatabaseToDomain(): Alarm {
    val frequency = this.frequency.split(STRING_SEPARATOR).map { Day.valueOf(it) }

    return Alarm(
            id,
            label,
            triggerTime,
            ringtone,
            frequency,
            vibrate,
            deleteOnDismiss,
            isActive,
            code
    )
}