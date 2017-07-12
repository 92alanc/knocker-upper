package com.ukdev.smartbuzz.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.ukdev.smartbuzz.R;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

/**
 * Test class for {@link Utils}
 *
 * @author Alan Camargo
 */
@RunWith(AndroidJUnit4.class)
public class UtilsTest {

    private String weekdaysString;
    private String weekendsString;
    private String multipleDaysString;
    private String singleDayString;

    private static final int[] WEEKDAYS = {
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY
    };

    private static final int[] WEEKENDS = {
            Calendar.SUNDAY,
            Calendar.SATURDAY
    };

    private static final int[] MULTIPLE_DAYS = {
            Calendar.MONDAY,
            Calendar.WEDNESDAY,
            Calendar.SATURDAY
    };

    private static final int[] SINGLE_DAY = {
            Calendar.SATURDAY
    };

    private Context context;

    @Before
    public void init() {
        context = InstrumentationRegistry.getTargetContext();
        weekdaysString = context.getString(R.string.week_days);
        weekendsString = context.getString(R.string.weekends);
        String mon = context.getString(R.string.monday);
        String wed = context.getString(R.string.wednesday);
        String sat = context.getString(R.string.saturday);
        multipleDaysString = String.format("%1$s, %2$s, %3$s", mon, wed, sat);
        singleDayString = context.getString(R.string.saturday);
    }

    @Test
    public void shouldConvertWeekdaysStringToIntArray() {
        int[] result = Utils.convertStringToIntArray(context, weekdaysString);
        for (int i = 0; i < result.length; i++)
            Assert.assertEquals(WEEKDAYS[i], result[i]);
    }

    @Test
    public void shouldConvertWeekendsStringToIntArray() {
        int[] result = Utils.convertStringToIntArray(context, weekendsString);
        for (int i = 0; i < result.length; i++)
            Assert.assertEquals(WEEKENDS[i], result[i]);
    }

    @Test
    public void shouldConvertMultipleDaysStringToIntArray() {
        int[] result = Utils.convertStringToIntArray(context, multipleDaysString);
        for (int i = 0; i < result.length; i++)
            Assert.assertEquals(MULTIPLE_DAYS[i], result[i]);
    }

    @Test
    public void shouldConvertSingleDayStringToIntArray() {
        int[] result = Utils.convertStringToIntArray(context, singleDayString);
        for (int i = 0; i < result.length; i++)
            Assert.assertEquals(SINGLE_DAY[i], result[i]);
    }

    @Test
    public void shouldConvertWeekdaysToString() {
        String result = Utils.convertIntArrayToString(context, WEEKDAYS);
        Assert.assertEquals(weekdaysString, result);
    }

    @Test
    public void shouldConvertWeekendsToString() {
        String result = Utils.convertIntArrayToString(context, WEEKENDS);
        Assert.assertEquals(weekendsString, result);
    }

    @Test
    public void shouldConvertMultipleDaysToString() {
        String result = Utils.convertIntArrayToString(context, MULTIPLE_DAYS);
        Assert.assertEquals(multipleDaysString, result);
    }

    @Test
    public void shouldConvertSingleDayToString() {
        String result = Utils.convertIntArrayToString(context, SINGLE_DAY);
        Assert.assertEquals(singleDayString, result);
    }

}
