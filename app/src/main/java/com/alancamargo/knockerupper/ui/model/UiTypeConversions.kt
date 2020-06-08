package com.alancamargo.knockerupper.ui.model

import android.net.Uri
import com.alancamargo.knockerupper.domain.entities.Alarm
import com.alancamargo.knockerupper.domain.entities.Code

fun Alarm.fromDomainToUi(): UiAlarm {
    val ringtone = if (this.ringtone != null)
        Uri.parse(this.ringtone)
    else
        null

    return UiAlarm(
            id,
            label,
            triggerTime,
            ringtone,
            frequency,
            vibrate, deleteOnDismiss,
            isActive,
            code?.fromDomainToUi()
    )
}

fun UiAlarm.fromUiToDomain(): Alarm {
    val ringtone = if (this.ringtone != null)
        this.ringtone.toString()
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
            code?.fromUiToDomain()
    )
}

fun Code.fromDomainToUi(): UiCode = UiCode(label, value)

fun UiCode.fromUiToDomain(): Code = Code(label, value)