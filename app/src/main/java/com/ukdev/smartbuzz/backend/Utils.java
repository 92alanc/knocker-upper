package com.ukdev.smartbuzz.backend;

import android.content.Context;
import android.media.AudioManager;
import com.ukdev.smartbuzz.model.Alarm;

public class Utils {

    public static long getNextValidTriggerTime(Alarm alarm) {
        return 0;
    }

    public static int getDefaultVolume(Context context) {
        return getMaxVolume(context) / 2;
    }

    public static int getMaxVolume(Context context) {
        AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
    }

    public static void killApp(Context context) {

    }

}
