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
        robot.launchFragment()
             .clickOnDismiss()
             .checkIfActivityIsDestroyed()
    }

}
