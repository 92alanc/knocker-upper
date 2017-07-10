package com.ukdev.smartbuzz.system;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.ObjectMocker;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * Test class for {@link AlarmHandler}
 *
 * @author Alan Camargo
 */
@RunWith(AndroidJUnit4.class)
public class AlarmHandlerTest {

    private AlarmHandler alarmHandler;

    /**
     * Initialises the test class
     */
    @Before
    public void init() {
        Context context = InstrumentationRegistry.getTargetContext();
        ObjectMocker objectMocker = new ObjectMocker();
        Alarm alarm = objectMocker.alarm();
        alarmHandler = new AlarmHandler(context, alarm);
    }

}
