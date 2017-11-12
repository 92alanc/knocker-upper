package com.ukdev.smartbuzz.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.fragments.TwoLinesThemePicker;
import com.ukdev.smartbuzz.util.PreferenceUtils;

/**
 * The settings activity
 *
 * @author Alan Camargo
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        replaceFragmentPlaceholders();
    }

    private void replaceFragmentPlaceholders() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        bindThemePicker(transaction);
        transaction.commit();
    }

    private void bindThemePicker(FragmentTransaction transaction) {
        String[] labels = getResources().getStringArray(R.array.themes);
        PreferenceUtils.Theme[] values = PreferenceUtils.Theme.values();
        TwoLinesThemePicker fragment = TwoLinesThemePicker.newInstance(labels, values);
        transaction.replace(R.id.placeholder_theme, fragment);
    }

}
