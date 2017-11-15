package com.ukdev.smartbuzz.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.fragments.TwoLinesDefaultFragment;
import com.ukdev.smartbuzz.fragments.TwoLinesThemePicker;
import com.ukdev.smartbuzz.listeners.OnFragmentInflatedListener;
import com.ukdev.smartbuzz.util.PreferenceUtils;
import com.ukdev.smartbuzz.util.ViewUtils;

/**
 * The settings activity
 *
 * @author Alan Camargo
 */
public class SettingsActivity extends AppCompatActivity
        implements OnFragmentInflatedListener,
                   TwoLinesDefaultFragment.TwoLinesChangeListener<PreferenceUtils.Theme> {

    private PreferenceUtils preferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(PreferenceUtils.FILE_NAME,
                                                                   MODE_PRIVATE);
        preferenceUtils = new PreferenceUtils(sharedPreferences);
        setTheme(preferenceUtils.getTheme().getRes());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        Activity activity = this;
        ViewUtils.showAds(activity, R.id.ad_view_settings);
        replaceFragmentPlaceholders();
    }

    @Override
    public void onFragmentInflated(Fragment fragment) {
        TwoLinesThemePicker themePicker = (TwoLinesThemePicker) fragment;
        themePicker.setDefaultSelectedItem();
    }

    @Override
    public void onChange(PreferenceUtils.Theme newValue) {
        preferenceUtils.setTheme(newValue);
        recreate();
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
        fragment.setOnFragmentInflatedListener(this);
        fragment.setChangeListener(this);
        fragment.setTitle(getString(R.string.theme));
        transaction.replace(R.id.placeholder_theme, fragment);
    }

}
