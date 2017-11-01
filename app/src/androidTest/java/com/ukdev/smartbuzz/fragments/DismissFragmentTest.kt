package com.ukdev.smartbuzz.fragments

import android.support.test.runner.AndroidJUnit4
import com.ukdev.smartbuzz.robots.DismissFragmentRobot
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DismissFragmentTest {

    private val robot = DismissFragmentRobot()

    @Test
    fun shouldFinishActivityWhenClickingOnDismiss() {
        val sleepCheckerMode = false
        robot.launchFragment(sleepCheckerMode)
             .clickOnDismiss()
             .checkIfActivityIsDestroyed()
    }

    @Test
    fun shouldDisplayCorrectTextWhenNotOnSleepCheckerMode() {
        val sleepCheckerMode = false
        robot.launchFragment(sleepCheckerMode)
             .validateDismissButtonText()
    }

    @Test
    fun shouldDisplayCorrectTextWhenOnSleepCheckerMode() {
        val sleepCheckerMode = true
        robot.launchFragment(sleepCheckerMode)
             .validateIAmAwakeButtonText()
    }

}