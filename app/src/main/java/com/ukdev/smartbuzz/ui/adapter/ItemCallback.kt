package com.ukdev.smartbuzz.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ukdev.smartbuzz.domain.model.Alarm

object ItemCallback : DiffUtil.ItemCallback<Alarm>() {

    override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem == newItem
    }

}