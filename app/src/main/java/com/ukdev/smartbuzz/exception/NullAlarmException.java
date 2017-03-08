package com.ukdev.smartbuzz.exception;

public class NullAlarmException extends Exception {

    public NullAlarmException() {
        super("The alarm cannot be null");
    }

}
