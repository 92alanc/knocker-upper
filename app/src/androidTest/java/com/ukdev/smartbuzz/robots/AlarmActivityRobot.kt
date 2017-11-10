package com.ukdev.smartbuzz.robots

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import com.ukdev.smartbuzz.R
import com.ukdev.smartbuzz.activities.AlarmActivity
import com.ukdev.smartbuzz.common.BaseActivityRobot
import com.ukdev.smartbuzz.database.AlarmDao
import com.ukdev.smartbuzz.model.Alarm
import org.junit.Rule

class AlarmActivityRobot : BaseActivityRobot() {

    private val alarm : Alarm
    private val context : Context = InstrumentationRegistry.getTargetContext()

    init {
        val dao = AlarmDao.getInstance(context)
        alarm = dao.select()[0]
    }

    @Rule
    @JvmField
    val rule = ActivityTestRule<AlarmActivity>(AlarmActivity::class.java, initialTouchMode,
                                               launchActivity)

    fun launchActivity(triggerSleepChecker: Boolean): AlarmActivityRobot {
        val intent = AlarmActivity.getIntent(context, alarm.id, triggerSleepChecker)
        rule.launchActivity(intent)
        return this
    }

    fun validateTitle(): AlarmActivityRobot {
        titleTextView().check(matches(isDisplayed()))
        titleTextView().check(matches(withText(alarm.name)))
        return this
    }

    private fun titleTextView(): ViewInteraction {
        return onView(withId(R.id.text_view_alarm_name))
    }

}