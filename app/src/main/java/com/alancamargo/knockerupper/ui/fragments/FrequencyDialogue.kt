package com.alancamargo.knockerupper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.alancamargo.knockerupper.R
import com.alancamargo.knockerupper.domain.entities.Day
import kotlinx.android.synthetic.main.dialogue_frequency.*

class FrequencyDialogue : DialogFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialogue_frequency, container, false)
    }

    fun getFrequency() = getCheckBoxes().filter {
        it.key.isChecked
    }.values.toList()

    private fun getCheckBoxes() = mapOf(
            monday to Day.MONDAY,
            tuesday to Day.TUESDAY,
            wednesday to Day.WEDNESDAY,
            thursday to Day.THURSDAY,
            friday to Day.FRIDAY,
            saturday to Day.SATURDAY,
            sunday to Day.SUNDAY
    )

}