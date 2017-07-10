package com.ukdev.smartbuzz.model;

import android.media.RingtoneManager;
import android.net.Uri;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;

/**
 * Object mocker for use in unit tests
 *
 * @author Alan Camargo
 */
public class ObjectMocker {

    /**
     * Mocks an {@link Alarm}
     * @return the mocked instance
     *         of {@link Alarm}
     */
    public Alarm alarm() {
        Time triggerTime = new Time();
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        AlarmBuilder alarmBuilder = new AlarmBuilder().setTitle("Title")
                                                      .setRingtoneUri(ringtoneUri)
                                                      .setTriggerTime(triggerTime)
                                                      .setSnoozeDuration(SnoozeDuration.FIVE_MINUTES);
        return alarmBuilder.build();
    }

}
