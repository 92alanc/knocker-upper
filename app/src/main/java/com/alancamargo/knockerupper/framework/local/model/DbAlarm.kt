package com.alancamargo.knockerupper.framework.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbAlarm(
        @PrimaryKey val id: String,
        val label: String,
        val triggerTime: Long,
        val ringtone: String?,
        val frequency: String,
        val vibrate: Boolean,
        val deleteOnDismiss: Boolean,
        val isActive: Boolean,
        val code: String?
)