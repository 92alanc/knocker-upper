package com.ukdev.smartbuzz.model;

import android.content.Context;

/**
 * Object mocker for use in unit tests
 *
 * @author Alan Camargo
 */
public class ObjectMocker {

    private Context context;

    /**
     * Default constructor for {@code ObjectMocker}
     * @param context the Android context
     */
    public ObjectMocker(Context context) {
        this.context = context;
    }

    /**
     * Mocks an {@link Alarm}
     * @return the mocked instance
     *         of {@link Alarm}
     */
    public Alarm alarm() {
        return new Alarm(context);
    }

}
