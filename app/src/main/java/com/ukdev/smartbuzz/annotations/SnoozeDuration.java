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
@IntDef({Time.ZERO, Time.FIVE_MINUTES, Time.TEN_MINUTES,
        Time.FIFTEEN_MINUTES, Time.TWENTY_MINUTES,
        Time.TWENTY_FIVE_MINUTES, Time.THIRTY_MINUTES})
public @interface SnoozeDuration { }
