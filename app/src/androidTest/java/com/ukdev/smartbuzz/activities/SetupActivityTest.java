package com.ukdev.smartbuzz.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ukdev.smartbuzz.robots.SetupActivityRobot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for {@link SetupActivity}
 *
 * @author Alan Camargo
 */
@RunWith(AndroidJUnit4.class)
public class SetupActivityTest {

    private static final boolean INITIAL_TOUCH_MODE = false;
    private static final boolean LAUNCH_ACTIVITY = false;

    @Rule
    public ActivityTestRule<SetupActivity> rule = new ActivityTestRule<>(SetupActivity.class,
                                                                         INITIAL_TOUCH_MODE,
                                                                         LAUNCH_ACTIVITY);

    private SetupActivityRobot robot;

    @Before
    public void setup() {
        robot = new SetupActivityRobot(rule);
    }

    @Test
    public void shouldClickOnSave() {
        robot.launchActivity()
             .clickOnSave();
    }

    @Test
    public void deleteActionButtonShouldBeVisibleWhenInEditMode() {

    }

    @Test
    public void deleteActionButtonShouldBeInvisibleWhenNotInEditMode() {

    }

}
