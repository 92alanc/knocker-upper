package com.ukdev.smartbuzz.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.fragments.RepetitionFragment;
import com.ukdev.smartbuzz.fragments.RingtoneFragment;
import com.ukdev.smartbuzz.fragments.SnoozeDurationFragment;
import com.ukdev.smartbuzz.misc.IntentExtra;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.AlarmBuilder;
import com.ukdev.smartbuzz.model.Ringtone;
import com.ukdev.smartbuzz.model.enums.Day;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;
import com.ukdev.smartbuzz.system.AlarmHandler;
import com.ukdev.smartbuzz.util.ViewUtils;
import com.ukdev.smartbuzz.view.CustomTimePicker;

import java.util.Calendar;

/**
 * The activity where alarms are set
 *
 * @author Alan Camargo
 */
public class SetupActivity extends AppCompatActivity {

    private Alarm alarm;
    private AlarmDao dao;
    private boolean editMode;
    private Context context;
    private CustomTimePicker timePicker;
    private EditText titleEditText;
    private FragmentManager fragmentManager;
    private ProgressBar progressBar;
    private RepetitionFragment repetitionFragment;
    private RingtoneFragment ringtoneFragment;
    private SnoozeDurationFragment snoozeDurationFragment;

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
        context = this;
        dao = AlarmDao.getInstance(context);
        final boolean defaultBooleanValue = false;
        editMode = getIntent().getBooleanExtra(IntentExtra.EDIT_MODE.toString(), defaultBooleanValue);
        fragmentManager = getSupportFragmentManager();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_setup);
        timePicker = (CustomTimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        titleEditText = (AppCompatEditText) findViewById(R.id.edit_text_title);
        setSaveButton();
        if (editMode) {
            final int defaultIntValue = 0;
            int id = getIntent().getIntExtra(IntentExtra.ID.toString(), defaultIntValue);
            alarm = dao.select(id);
            titleEditText.setText(alarm.getTitle());
            timePicker.setHour(alarm.getTriggerTime().get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(alarm.getTriggerTime().get(Calendar.MINUTE));
        }
        Activity activity = this;
        ViewUtils.showAds(activity, R.id.ad_view_setup);
    }

    private void loadFragments() {
        loadRepetitionFragment();
        loadSnoozeDurationFragment();
        loadRingtoneFragment();
    }

    private void loadRepetitionFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        repetitionFragment = new RepetitionFragment();
        if (editMode)
            setEditModeArgs(repetitionFragment);
        transaction.replace(R.id.placeholder_repetition, repetitionFragment);
        transaction.commit();
    }

    private void loadRingtoneFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ringtoneFragment = new RingtoneFragment();
        if (editMode)
            setEditModeArgs(ringtoneFragment);
        transaction.replace(R.id.placeholder_ringtone, ringtoneFragment);
        transaction.commit();
    }

    private void loadSnoozeDurationFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        snoozeDurationFragment = new SnoozeDurationFragment();
        if (editMode)
            setEditModeArgs(snoozeDurationFragment);
        transaction.replace(R.id.placeholder_snooze_duration, snoozeDurationFragment);
        transaction.commit();
    }

    private void setEditModeArgs(Fragment fragment) {
        Bundle args = new Bundle();
        args.putBoolean(IntentExtra.EDIT_MODE.toString(), editMode);
        args.putInt(IntentExtra.ID.toString(), alarm.getId());
        fragment.setArguments(args);
    }

    private boolean save() {
        boolean success;
        progressBar.setVisibility(View.VISIBLE);
        AlarmBuilder alarmBuilder = new AlarmBuilder(context);
        int nextAvailableId = dao.getLastId() + 1;
        int id = getIntent().getIntExtra(IntentExtra.ID.toString(), nextAvailableId);
        alarmBuilder.setId(id);
        alarmBuilder.setTitle(getAlarmTitle());
        alarmBuilder.setTriggerTime(getTriggerTime());
        alarmBuilder.setRepetition(getRepetition());
        alarmBuilder.setRingtone(getRingtone());
        alarmBuilder.setSnoozeDuration(getSnoozeDuration());

        Alarm alarm = alarmBuilder.build();
        AlarmHandler alarmHandler = new AlarmHandler(context, alarm);
        progressBar.setVisibility(View.GONE);
        if (editMode) {
            alarmHandler.updateAlarm();
            success = dao.update(alarm);
        } else {
            alarmHandler.setAlarm();
            success = dao.insert(alarm);
        }
        return success;
    }

    private String getAlarmTitle() {
        return titleEditText.getText().toString();
    }

    private Calendar getTriggerTime() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        Calendar triggerTime = Calendar.getInstance();
        triggerTime.set(Calendar.HOUR_OF_DAY, hour);
        triggerTime.set(Calendar.MINUTE, minute);
        return triggerTime;
    }

    private Day[] getRepetition() {
        return repetitionFragment.getSelectedRepetition();
    }

    private Ringtone getRingtone() {
        return ringtoneFragment.getSelectedRingtone();
    }

    private SnoozeDuration getSnoozeDuration() {
        return snoozeDurationFragment.getSelectedSnoozeDuration();
    }

    private void setSaveButton() {
        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.fab_setup);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!save()) {
                    Snackbar snackbar = Snackbar.make(view,
                                                      R.string.error_save_alarm,
                                                      Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            save();
                        }
                    });
                } else {
                    Toast.makeText(context, R.string.alarm_saved, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}
