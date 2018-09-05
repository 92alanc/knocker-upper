package com.ukdev.smartbuzz.annotations;

import android.support.annotation.IntDef;

import com.ukdev.smartbuzz.model.Time;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A valid snooze duration
 *
 * @author Alan Camargo
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.LOCAL_VARIABLE, ElementType.FIELD,
        ElementType.PARAMETER, ElementType.METHOD})
@IntDef({(int) Time.ZERO, (int) Time.FIVE_MINUTES, (int) Time.TEN_MINUTES,
        (int) Time.FIFTEEN_MINUTES, (int) Time.TWENTY_MINUTES,
        (int) Time.TWENTY_FIVE_MINUTES, (int) Time.THIRTY_MINUTES})
public @interface SnoozeDuration { }
