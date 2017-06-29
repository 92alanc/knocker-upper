package com.ukdev.smartbuzz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.fragments.RepetitionFragment;
import com.ukdev.smartbuzz.fragments.RingtoneFragment;
import com.ukdev.smartbuzz.fragments.SnoozeDurationFragment;
import com.ukdev.smartbuzz.listeners.OnFragmentAttachListener;
import com.ukdev.smartbuzz.util.ViewUtils;
import com.ukdev.smartbuzz.view.CustomTimePicker;

/**
 * The activity where alarms are set
 *
 * @author Alan Camargo
 */
public class SetupActivity extends AppCompatActivity implements OnFragmentAttachListener {

    private CustomTimePicker timePicker;
    private FragmentManager fragmentManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        initialiseComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFragments();
    }

    private void initialiseComponents() {
        fragmentManager = getSupportFragmentManager();
        Activity activity = this;
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_setup);
        timePicker = (CustomTimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        ViewUtils.showAds(activity, R.id.ad_view_setup);
    }

    private void loadFragments() {
        loadRepetitionFragment();
        loadSnoozeDurationFragment();
        loadRingtoneFragment();
    }

    private void loadRepetitionFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        RepetitionFragment fragment = new RepetitionFragment();
        transaction.replace(R.id.placeholder_repetition, fragment);
        transaction.commit();
    }

    private void loadSnoozeDurationFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SnoozeDurationFragment fragment = new SnoozeDurationFragment();
        transaction.replace(R.id.placeholder_snooze_duration, fragment);
        transaction.commit();
    }

    private void loadRingtoneFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        RingtoneFragment fragment = new RingtoneFragment();
        transaction.replace(R.id.placeholder_ringtone, fragment);
        transaction.commit();
    }

    /**
     * Method called when a fragment starts loading
     */
    @Override
    public void onLoadFragment() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Method called when a fragment is fully attached
     */
    @Override
    public void onAttachFragment() {
        progressBar.setVisibility(View.GONE);
    }

}
