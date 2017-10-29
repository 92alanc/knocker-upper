package com.ukdev.smartbuzz.listeners;

import android.media.AudioManager;

/**
 * A listener for audio focus changes
 *
 * @author Alan Camargo
 */
public class AudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {

    private final AudioManager manager;
    private final int volume;

    /**
     * Default constructor for {@code AudioFocusChangeListener}
     * @param manager the audio manager
     * @param volume the playback volume
     */
    public AudioFocusChangeListener(AudioManager manager, int volume) {
        this.manager = manager;
        this.volume = volume;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        final int index = 1;
        final int flags = 0;
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                manager.setStreamVolume(AudioManager.STREAM_ALARM, index, flags);
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                manager.setStreamVolume(AudioManager.STREAM_ALARM,
                        volume,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                break;
        }
    }

}
