package com.alancamargo.knockerupper.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.alancamargo.knockerupper.ui.model.UiAlarm

object ItemCallback : DiffUtil.ItemCallback<UiAlarm>() {

    override fun areItemsTheSame(oldItem: UiAlarm, newItem: UiAlarm): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UiAlarm, newItem: UiAlarm): Boolean {
        return oldItem == newItem
    }

}