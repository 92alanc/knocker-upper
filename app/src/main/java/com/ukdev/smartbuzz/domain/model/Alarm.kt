package com.ukdev.smartbuzz.domain.model

import java.time.DayOfWeek

data class Alarm(
        val label: String,
        val triggerTime: Long,
        val frequency: List<DayOfWeek>,
        val isActive: Boolean,
        val code: String?
) {

    fun hasCode() = code != null

}