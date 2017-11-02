package com.ukdev.smartbuzz.robots

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.android21buttons.fragmenttestrule.FragmentTestRule
import com.ukdev.smartbuzz.R
import com.ukdev.smartbuzz.activities.AlarmActivity
import com.ukdev.smartbuzz.common.BaseFragmentRobot
import com.ukdev.smartbuzz.fragments.DismissFragment
import org.junit.Assert.assertTrue
import org.junit.Rule

class DismissFragmentRobot : BaseFragmentRobot() {

    @Rule
    @JvmField
    val rule = FragmentTestRule<AlarmActivity, DismissFragment>(AlarmActivity::class.java,
                                                                DismissFragment::class.java,
                                                                initialTouchMode,
                                                                launchActivity,
                                                                launchFragment)

    // FIXME: not launching fragment
    fun launchFragment(sleepCheckerMode: Boolean): DismissFragmentRobot {
        rule.launchFragment(fragment(sleepCheckerMode))
        return this
    }

    fun clickOnDismiss(): DismissFragmentRobot {
        dismissButton().perform(click())
        return this
    }

    fun checkIfActivityIsDestroyed(): DismissFragmentRobot {
        Thread.sleep(500)
        assertTrue(rule.fragment.activity.isDestroyed)
        return this
    }

    fun validateDismissButtonText(): DismissFragmentRobot {
        dismissButton().check(matches(withText(R.string.dismiss)))
        return this
    }

    fun validateIAmAwakeButtonText(): DismissFragmentRobot {
        dismissButton().check(matches(withText(R.string.i_am_awake)))
        return this
    }

    private fun fragment(sleepCheckerMode: Boolean): DismissFragment {
        val fragment = DismissFragment.newInstance(sleepCheckerMode)
        fragment.setOnFragmentInflatedListener {
            fragment.setOnClickListener { fragment.activity.finish() }
        }
        return fragment
    }

    private fun dismissButton(): ViewInteraction {
        return onView(withId(R.id.btDismiss))
    }

}