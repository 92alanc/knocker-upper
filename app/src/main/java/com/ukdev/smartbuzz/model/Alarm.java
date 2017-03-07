package com.ukdev.smartbuzz.model;

import java.util.Calendar;

public class Alarm {

    private int id;
    private String title;
    private Calendar triggerTime;
    private int snoozeDuration;
    private Day[] repetition;
    private RingtoneWrapper ringtone;
    private String text;
    private boolean sleepCheckerOn;
    private boolean active;

}
