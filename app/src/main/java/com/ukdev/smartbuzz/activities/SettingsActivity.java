package com.ukdev.smartbuzz.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import com.ukdev.smartbuzz.R;

/**
 * The settings activity
 *
 * @author Alan Camargo
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: update deprecated method
        addPreferencesFromResource(R.xml.preferences);
    }

}
