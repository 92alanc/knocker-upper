package com.alancamargo.knockerupper.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alancamargo.knockerupper.domain.model.Alarm
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_alarm.*

class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

    override val containerView: View? = itemView

    fun bindTo(alarm: Alarm) {
        txtLabel.text = alarm.label
        // TODO: bind the rest of the data
    }

}