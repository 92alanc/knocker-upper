package com.alancamargo.knockerupper.framework.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbAlarm(
        @PrimaryKey val id: String,
        val label: String,
        val triggerTime: Long,
        val frequency: String,
        val isActive: Boolean,
        val code: String?
)