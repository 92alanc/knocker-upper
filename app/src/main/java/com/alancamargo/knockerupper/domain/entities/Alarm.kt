package com.alancamargo.knockerupper.domain.entities

data class Alarm(
        val id: String,
        val label: String,
        val triggerTime: Long,
        val ringtone: String?,
        val frequency: List<Day>,
        val vibrate: Boolean,
        val deleteOnDismiss: Boolean,
        val isActive: Boolean,
        val code: Code?
) {

    fun hasCode() = code != null

}