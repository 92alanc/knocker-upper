package com.ukdev.smartbuzz.robots;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.misc.IntentExtra;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class SetupActivityRobot {

    private ActivityTestRule<? extends Activity> rule;

    public SetupActivityRobot(ActivityTestRule<? extends Activity> rule) {
        this.rule = rule;
    }

    public SetupActivityRobot launchActivity() {
        return launchActivity(false);
    }

    public SetupActivityRobot launchActivityInEditMode() {
        return launchActivity(true);
    }

    public SetupActivityRobot assertDeleteActionButtonIsVisible() {
        onView(withId(R.id.item_delete)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        return this;
    }

    public SetupActivityRobot clickOnSave() {
        onView(withId(R.id.fab_setup)).perform(click());
        return this;
    }

    public SetupActivityRobot clickOnDelete() {
        onView(withId(R.id.item_delete)).perform(click());
        return this;
    }

    private SetupActivityRobot launchActivity(boolean editMode) {
        rule.launchActivity(new Intent().putExtra(IntentExtra.EDIT_MODE.toString(), editMode));
        return this;
    }

}
