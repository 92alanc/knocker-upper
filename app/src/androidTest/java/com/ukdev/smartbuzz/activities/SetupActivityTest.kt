package com.ukdev.smartbuzz.activities

import android.support.test.runner.AndroidJUnit4
import com.ukdev.smartbuzz.common.BaseTest
import com.ukdev.smartbuzz.robots.SetupActivityRobot
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SetupActivityTest : BaseTest() {

    private val robot = SetupActivityRobot()

    @After
    fun after() {
        clearDatabase()
    }

    @Test
    fun shouldLaunchMainActivityWhenClickingOnSave() {
        robot.launchActivity()
             .clickOnSave()
             .checkIfLeadsToMainActivity()
    }

}