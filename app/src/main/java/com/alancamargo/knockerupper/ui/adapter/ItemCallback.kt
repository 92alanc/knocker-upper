package com.alancamargo.knockerupper.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.alancamargo.knockerupper.domain.model.Alarm

object ItemCallback : DiffUtil.ItemCallback<Alarm>() {

    override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem == newItem
    }

}