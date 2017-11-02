package com.ukdev.smartbuzz.robots

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import com.android21buttons.fragmenttestrule.FragmentTestRule
import com.ukdev.smartbuzz.R
import com.ukdev.smartbuzz.activities.AlarmActivity
import com.ukdev.smartbuzz.common.BaseFragmentRobot
import com.ukdev.smartbuzz.fragments.SnoozeFragment
import com.ukdev.smartbuzz.model.enums.SnoozeDuration
import org.junit.Assert.assertTrue
import org.junit.Rule

class SnoozeFragmentRobot : BaseFragmentRobot() {

    @Rule
    @JvmField
    val rule = FragmentTestRule<AlarmActivity, SnoozeFragment>(AlarmActivity::class.java,

                                                               SnoozeFragment::class.java,
                                                               initialTouchMode,
                                                               launchActivity,
                                                               launchFragment)

    // FIXME: not launching fragment
    fun launchFragment(snoozeDuration: SnoozeDuration): SnoozeFragmentRobot {
        rule.launchFragment(fragment(snoozeDuration))
        return this
    }

    fun clickOnSnooze(): SnoozeFragmentRobot {
        snoozeButton().perform(click())
        return this
    }

    fun checkIfActivityIsDestroyed(): SnoozeFragmentRobot {
        Thread.sleep(500)
        assertTrue(rule.fragment.activity.isDestroyed)
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

    private fun fragment(snoozeDuration: SnoozeDuration): SnoozeFragment {
        val fragment = SnoozeFragment()
        fragment.setOnFragmentInflatedListener {
            fragment.setButtonText(snoozeDuration)
            fragment.setOnClickListener { fragment.activity.finish() }
        }
        return fragment
    }

    private fun snoozeButton(): ViewInteraction {
        return onView(withId(R.id.btSnooze))
    }

}