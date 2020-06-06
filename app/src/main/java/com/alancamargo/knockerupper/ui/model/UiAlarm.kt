package com.alancamargo.knockerupper.ui.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.DayOfWeek

@Parcelize
data class UiAlarm(
        val id: String,
        val label: String,
        val triggerTime: Long,
        val ringtone: Uri?,
        val frequency: List<DayOfWeek>,
        val vibrate: Boolean,
        val deleteOnDismiss: Boolean,
        val isActive: Boolean,
        val code: String?
) : Parcelable