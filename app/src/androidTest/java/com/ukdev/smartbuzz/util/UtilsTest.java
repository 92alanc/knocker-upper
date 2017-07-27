package com.ukdev.smartbuzz.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.SparseBooleanArray;
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
    private SparseBooleanArray sparseBooleanArray;

    private static final Integer[] WEEKDAYS = {
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY
    };

    private static final Integer[] WEEKENDS = {
            Calendar.SUNDAY,
            Calendar.SATURDAY
    };

    private static final Integer[] MULTIPLE_DAYS = {
            Calendar.MONDAY,
            Calendar.WEDNESDAY,
            Calendar.SATURDAY
    };

    private static final Integer[] SINGLE_DAY = {
            Calendar.SATURDAY
    };

    private Context context;

    /**
     * Initialises the test class
     */
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
        sparseBooleanArray = new SparseBooleanArray();
        populateSparseBooleanArray(sparseBooleanArray);
    }

    /**
     * Validates the {@code convertStringToIntArray} method
     * with a weekdays input
     * @see Utils#convertStringToIntArray(Context, String)
     */
    @Test
    public void shouldConvertWeekdaysStringToIntArray() {
        Integer[] result = Utils.convertStringToIntArray(context, weekdaysString);
        for (int i = 0; i < result.length; i++)
            Assert.assertEquals(WEEKDAYS[i], result[i]);
    }

    /**
     * Validates the {@code convertStringToIntArray} method
     * with a weekends input
     * @see Utils#convertStringToIntArray(Context, String)
     */
    @Test
    public void shouldConvertWeekendsStringToIntArray() {
        Integer[] result = Utils.convertStringToIntArray(context, weekendsString);
        for (int i = 0; i < result.length; i++)
            Assert.assertEquals(WEEKENDS[i], result[i]);
    }

    /**
     * Validates the {@code convertStringToIntArray} method
     * with a multiple days input
     * @see Utils#convertStringToIntArray(Context, String)
     */
    @Test
    public void shouldConvertMultipleDaysStringToIntArray() {
        Integer[] result = Utils.convertStringToIntArray(context, multipleDaysString);
        for (int i = 0; i < result.length; i++)
            Assert.assertEquals(MULTIPLE_DAYS[i], result[i]);
    }

    /**
     * Validates the {@code convertStringToIntArray} method
     * with a single day input
     * @see Utils#convertStringToIntArray(Context, String)
     */
    @Test
    public void shouldConvertSingleDayStringToIntArray() {
        Integer[] result = Utils.convertStringToIntArray(context, singleDayString);
        for (int i = 0; i < result.length; i++)
            Assert.assertEquals(SINGLE_DAY[i], result[i]);
    }

    /**
     * Validates the {@code convertIntArrayToString} method
     * with a weekdays input
     * @see Utils#convertIntArrayToString(Context, Integer[])
     */
    @Test
    public void shouldConvertWeekdaysToString() {
        String result = Utils.convertIntArrayToString(context, WEEKDAYS);
        Assert.assertEquals(weekdaysString, result);
    }

    /**
     * Validates the {@code convertIntArrayToString} method
     * with a weekends input
     * @see Utils#convertIntArrayToString(Context, Integer[])
     */
    @Test
    public void shouldConvertWeekendsToString() {
        String result = Utils.convertIntArrayToString(context, WEEKENDS);
        Assert.assertEquals(weekendsString, result);
    }

    /**
     * Validates the {@code convertIntArrayToString} method
     * with a multiple days input
     * @see Utils#convertIntArrayToString(Context, Integer[])
     */
    @Test
    public void shouldConvertMultipleDaysToString() {
        String result = Utils.convertIntArrayToString(context, MULTIPLE_DAYS);
        Assert.assertEquals(multipleDaysString, result);
    }

    /**
     * Validates the {@code convertIntArrayToString} method
     * with a single day input
     * @see Utils#convertIntArrayToString(Context, Integer[])
     */
    @Test
    public void shouldConvertSingleDayToString() {
        String result = Utils.convertIntArrayToString(context, SINGLE_DAY);
        Assert.assertEquals(singleDayString, result);
    }

    /**
     * Validates the {@code convertIntArrayToSparseBooleanArray} method
     * @see Utils#convertIntArrayToSparseBooleanArray(Integer[])
     */
    @Test
    public void shouldConvertIntArrayToSparseBooleanArray() {
        SparseBooleanArray result = Utils.convertIntArrayToSparseBooleanArray(MULTIPLE_DAYS);
        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(MULTIPLE_DAYS[i], (Integer) result.keyAt(i));
    }

    /**
     * Validates the {@code convertSparseBooleanArrayToIntArray} method
     * @see Utils#convertSparseBooleanArrayToIntArray(SparseBooleanArray)
     */
    @Test
    public void shouldConvertSparseBooleanArrayToIntArray() {
        Integer[] result = Utils.convertSparseBooleanArrayToIntArray(sparseBooleanArray);
        for (int i = 0; i < result.length; i++)
            Assert.assertEquals((Integer) sparseBooleanArray.keyAt(i), result[i]);
    }

    private void populateSparseBooleanArray(SparseBooleanArray sparseBooleanArray) {
        sparseBooleanArray.put(Calendar.MONDAY, true);
        sparseBooleanArray.put(Calendar.WEDNESDAY, true);
        sparseBooleanArray.put(Calendar.SATURDAY, true);
    }

}
