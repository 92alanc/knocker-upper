package com.ukdev.smartbuzz.activities

import android.support.test.runner.AndroidJUnit4
import com.ukdev.smartbuzz.robots.AlarmActivityRobot
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmActivityTest {

    private val robot = AlarmActivityRobot()

    @Test
    fun shouldDisplayCorrectTitle() {
        val triggerSleepChecker = false
        robot.launchActivity(triggerSleepChecker)
             .validateTitle()
    }

    @Test
    fun shouldDisplayCorrectText() {
        val triggerSleepChecker = false
        robot.launchActivity(triggerSleepChecker)
             .validateText()
    }

}