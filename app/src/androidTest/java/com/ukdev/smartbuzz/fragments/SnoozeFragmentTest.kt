package com.ukdev.smartbuzz.fragments

import android.support.test.runner.AndroidJUnit4
import com.ukdev.smartbuzz.model.enums.SnoozeDuration
import com.ukdev.smartbuzz.robots.SnoozeFragmentRobot
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SnoozeFragmentTest {

    private val robot = SnoozeFragmentRobot()

    @Test
    fun shouldFinishActivityWhenClickingOnSnooze() {
        robot.launchFragment(SnoozeDuration.FIVE_MINUTES)
             .clickOnSnooze()
             .checkIfActivityIsDestroyed()
    }

    @Test
    fun shouldDisplaySnoozeButtonWhenSnoozeDurationIs5Min() {
        robot.launchFragment(SnoozeDuration.FIVE_MINUTES)
             .checkIfButtonIsVisible()
    }

    @Test
    fun shouldDisplayCorrectTextWhenSnoozeDurationIs5Min() {
        robot.launchFragment(SnoozeDuration.FIVE_MINUTES)
             .checkIf5MinIsDisplayed()
    }

    @Test
    fun shouldDisplaySnoozeButtonWhenSnoozeDurationIs10Min() {
        robot.launchFragment(SnoozeDuration.TEN_MINUTES)
                .checkIfButtonIsVisible()
    }

    @Test
    fun shouldDisplayCorrectTextWhenSnoozeDurationIs10Min() {
        robot.launchFragment(SnoozeDuration.TEN_MINUTES)
                .checkIf10MinIsDisplayed()
    }

    @Test
    fun shouldDisplaySnoozeButtonWhenSnoozeDurationIs15Min() {
        robot.launchFragment(SnoozeDuration.FIFTEEN_MINUTES)
                .checkIfButtonIsVisible()
    }

    @Test
    fun shouldDisplayCorrectTextWhenSnoozeDurationIs15Min() {
        robot.launchFragment(SnoozeDuration.FIFTEEN_MINUTES)
                .checkIf15MinIsDisplayed()
    }

    @Test
    fun shouldDisplaySnoozeButtonWhenSnoozeDurationIs20Min() {
        robot.launchFragment(SnoozeDuration.TWENTY_MINUTES)
                .checkIfButtonIsVisible()
    }

    @Test
    fun shouldDisplayCorrectTextWhenSnoozeDurationIs20Min() {
        robot.launchFragment(SnoozeDuration.TWENTY_MINUTES)
                .checkIf20MinIsDisplayed()
    }

    @Test
    fun shouldDisplaySnoozeButtonWhenSnoozeDurationIs25Min() {
        robot.launchFragment(SnoozeDuration.TWENTY_FIVE_MINUTES)
                .checkIfButtonIsVisible()
    }

    @Test
    fun shouldDisplayCorrectTextWhenSnoozeDurationIs25Min() {
        robot.launchFragment(SnoozeDuration.TWENTY_FIVE_MINUTES)
                .checkIf25MinIsDisplayed()
    }

    @Test
    fun shouldDisplaySnoozeButtonWhenSnoozeDurationIs30Min() {
        robot.launchFragment(SnoozeDuration.THIRTY_MINUTES)
                .checkIfButtonIsVisible()
    }

    @Test
    fun shouldDisplayCorrectTextWhenSnoozeDurationIs30Min() {
        robot.launchFragment(SnoozeDuration.THIRTY_MINUTES)
                .checkIf30MinIsDisplayed()
    }

    @Test
    fun shouldNotDisplaySnoozeButtonWhenSnoozeDurationIsZero() {
        robot.launchFragment(SnoozeDuration.OFF)
             .checkIfButtonIsNotVisible()
    }

}