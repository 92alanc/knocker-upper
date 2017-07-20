package com.ukdev.smartbuzz.database;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.ObjectMocker;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for {@link AlarmDao}
 *
 * @author Alan Camargo
 */
@RunWith(AndroidJUnit4.class)
public class AlarmDaoTest {

    private AlarmDao alarmDao;
    private ObjectMocker objectMocker;

    /**
     * Initialises the test class
     */
    @Before
    public void init() {
        Context context = InstrumentationRegistry.getTargetContext();
        alarmDao = AlarmDao.getInstance(context);
        objectMocker = new ObjectMocker();
        Alarm alarm = objectMocker.alarm();
        alarmDao.insert(alarm);
    }

    /**
     * Validates the insert method
     * @see AlarmDao#insert(Alarm)
     */
    @Test
    public void shouldInsert() {
        Alarm alarm = objectMocker.alarm();
        Assert.assertTrue(alarmDao.insert(alarm) > 0);
    }

    /**
     * Validates the delete method
     * @see AlarmDao#delete(Alarm)
     */
    @Test
    public void shouldDelete() {
        int lastId = alarmDao.getLastId();
        Alarm alarmToDelete = alarmDao.select(lastId);
        Assert.assertTrue(alarmDao.delete(alarmToDelete));
    }

}
