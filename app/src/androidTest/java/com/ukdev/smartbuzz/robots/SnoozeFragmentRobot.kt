package com.ukdev.smartbuzz.robots

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import com.android21buttons.fragmenttestrule.FragmentTestRule
import com.ukdev.smartbuzz.R
import com.ukdev.smartbuzz.activities.TestActivity
import com.ukdev.smartbuzz.fragments.SnoozeFragment
import org.junit.Assert.assertTrue
import org.junit.Rule

class SnoozeFragmentRobot {

    @Rule
    @JvmField
    val rule = FragmentTestRule(TestActivity::class.java, SnoozeFragment::class.java)

    fun launchFragment(snoozeDuration: Long): SnoozeFragmentRobot {
        rule.launchActivity(Intent())
        rule.launchFragment(fragment(snoozeDuration))
        return this
    }

    fun clickOnSnooze(): SnoozeFragmentRobot {
        snoozeButton().perform(click())
        return this
    }

    fun checkIfActivityIsDestroyed(): SnoozeFragmentRobot {
        Thread.sleep(500)
        assertTrue(rule.fragment.activity!!.isDestroyed)
        return this
    }

    fun checkIf5MinIsDisplayed(): SnoozeFragmentRobot {
        snoozeButton().check(matches(withText(R.string.five_min)))
        return this
    }

    fun checkIf10MinIsDisplayed(): SnoozeFragmentRobot {
        snoozeButton().check(matches(withText(R.string.ten_min)))
        return this
    }

    fun checkIf15MinIsDisplayed(): SnoozeFragmentRobot {
        snoozeButton().check(matches(withText(R.string.fifteen_min)))
        return this
    }

    fun checkIf20MinIsDisplayed(): SnoozeFragmentRobot {
        snoozeButton().check(matches(withText(R.string.twenty_min)))
        return this
    }

    fun checkIf25MinIsDisplayed(): SnoozeFragmentRobot {
        snoozeButton().check(matches(withText(R.string.twenty_five_min)))
        return this
    }

    fun checkIf30MinIsDisplayed(): SnoozeFragmentRobot {
        snoozeButton().check(matches(withText(R.string.thirty_min)))
        return this
    }

    fun checkIfButtonIsVisible(): SnoozeFragmentRobot {
        snoozeButton().check(matches(isDisplayed()))
        return this
    }

    fun checkIfButtonIsNotVisible(): SnoozeFragmentRobot {
        snoozeButton().check(matches(withEffectiveVisibility(Visibility.GONE)))
        return this
    }

    private fun fragment(snoozeDuration: Long): SnoozeFragment {
        val fragment = SnoozeFragment()
        fragment.setOnFragmentInflatedListener {
            fragment.setButtonText(snoozeDuration)
            fragment.setOnClickListener { fragment.activity!!.finish() }
        }
        return fragment
    }

    private fun snoozeButton(): ViewInteraction = onView(withId(R.id.btSnooze))

}