package com.alancamargo.knockerupper.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.alancamargo.knockerupper.R
import com.alancamargo.knockerupper.ui.model.UiAlarm

class AlarmDetailsActivity : AppCompatActivity(R.layout.activity_alarm_details) {

    companion object {
        private const val KEY_ALARM = "alarm"

        fun getIntent(context: Context) = Intent(
                context, AlarmDetailsActivity::class.java
        )

        fun getIntent(context: Context, alarm: UiAlarm): Intent {
            return Intent(context, AlarmDetailsActivity::class.java).apply {
                putExtra(KEY_ALARM, alarm)
            }
        }
    }

}