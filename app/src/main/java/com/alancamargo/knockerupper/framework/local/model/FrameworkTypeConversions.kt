package com.alancamargo.knockerupper.framework.local.model

import com.alancamargo.knockerupper.domain.entities.Alarm
import com.alancamargo.knockerupper.domain.entities.Code
import com.alancamargo.knockerupper.domain.entities.Day

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
            code?.label,
            code?.value
    )
}

fun DbAlarm.fromDatabaseToDomain(): Alarm {
    val frequency = this.frequency.split(STRING_SEPARATOR).map { Day.valueOf(it) }
    val code = if (codeLabel != null && codeValue != null)
        Code(codeLabel, codeValue)
    else
        null

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