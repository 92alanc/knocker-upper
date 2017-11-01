package com.ukdev.smartbuzz.activities

import android.support.test.runner.AndroidJUnit4
import com.ukdev.smartbuzz.robots.MainActivityRobot
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val robot = MainActivityRobot()

    @Test
    fun shouldLaunchSetupActivityWhenClickingOnAdd() {
        robot.launchActivity()
             .clickOnAdd()
             .checkIfLeadsToSetupActivity()
    }

}