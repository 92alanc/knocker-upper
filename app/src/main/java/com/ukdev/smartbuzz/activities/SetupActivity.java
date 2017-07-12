package com.ukdev.smartbuzz.activities;

import android.support.v7.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.fragments.*;
import com.ukdev.smartbuzz.misc.IntentExtra;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.AlarmBuilder;
import com.ukdev.smartbuzz.model.Time;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;
import com.ukdev.smartbuzz.system.AlarmHandler;
import com.ukdev.smartbuzz.util.Utils;

/**
 * The activity where alarms are set
 *
 * @author Alan Camargo
 */
public class SetupActivity extends AppCompatActivity implements View.OnClickListener {

    private AlarmDao dao;
    private AlarmHandler alarmHandler;
    private boolean editMode;
    private Context context;
    private int alarmId;
    private TwoLinesEditText titleFragment;
    private TwoLinesTimePicker timePickerFragment;
    private TwoLinesDayOfTheWeek repetitionFragment;
    private TwoLinesRadioGroup snoozeDurationFragment;
    private TwoLinesRingtone ringtoneFragment;
    private TwoLinesSeekBar volumeFragment;
    private TwoLinesSwitch vibrationFragment;
    private TwoLinesSwitch sleepCheckerFragment;
    private TwoLinesMemo textFragment;
    private View.OnClickListener onClickListener;

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
        editMode = getIntent().getBooleanExtra(IntentExtra.EDIT_MODE.toString(), false);
        replaceFragmentPlaceholders();
        if (editMode) {
            alarmId = getIntent().getIntExtra(IntentExtra.ID.toString(), 0);
            setFragmentValues();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setup, menu);
        MenuItem item = menu.getItem(0);
        if (editMode)
            item.setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_delete) {
            AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(context);
            dialogueBuilder.setTitle(R.string.delete_alarm);
            dialogueBuilder.setMessage(R.string.confirm_delete);
            dialogueBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    delete();
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                }
            });
            dialogueBuilder.setNegativeButton(R.string.no, null);
            dialogueBuilder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialiseComponents() {
        context = this;
        onClickListener = this;
        dao = AlarmDao.getInstance(context);
        setSaveButton();
        setTitleFragment();
        setTimePickerFragment();
        setRepetitionFragment();
        setSnoozeDurationFragment();
        setRingtoneFragment();
        setVolumeFragment();
        setVibrationFragment();
        setSleepCheckerFragment();
        setTextFragment();
    }

    private void delete() {
        final Alarm alarm = dao.select(alarmId);
        if (!dao.delete(alarm)) {
            Snackbar snackbar = Snackbar.make(getCurrentFocus(),
                                              R.string.error_delete_alarm,
                                              Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.error_delete_alarm, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dao.delete(alarm))
                        ackDelete();
                }
            });
            snackbar.show();
        } else
            ackDelete();
    }

    private void ackDelete() {
        alarmHandler.cancelAlarm();
        Toast.makeText(context, R.string.alarm_deleted, Toast.LENGTH_SHORT)
             .show();
    }

    private void replaceFragmentPlaceholders() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.placeholder_title, titleFragment);
        transaction.replace(R.id.placeholder_time_picker, timePickerFragment);
        transaction.replace(R.id.placeholder_repetition, repetitionFragment);
        transaction.replace(R.id.placeholder_snooze_duration, snoozeDurationFragment);
        transaction.replace(R.id.placeholder_ringtone, ringtoneFragment);
        transaction.replace(R.id.placeholder_volume, volumeFragment);
        transaction.replace(R.id.placeholder_vibrate, vibrationFragment);
        transaction.replace(R.id.placeholder_sleep_checker, sleepCheckerFragment);
        transaction.replace(R.id.placeholder_text, textFragment);
        transaction.commit();
    }

    private void setSaveButton() {
        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.fab_setup);
        saveButton.setOnClickListener(onClickListener);
    }

    private void setTitleFragment() {
        titleFragment = new TwoLinesEditText();
        String title = getString(R.string.title);
        titleFragment.setTitle(title);
        titleFragment.setChangeListener(new TwoLinesDefaultFragment.TwoLinesChangeListener<String>() {
            @Override
            public void onChange(String newValue) {
                setTitle(newValue); // FIXME: not setting activity title
            }
        });
    }

    private void setTimePickerFragment() {
        timePickerFragment = new TwoLinesTimePicker();
        String title = getString(R.string.time);
        timePickerFragment.setTitle(title);
    }

    private void setRepetitionFragment() {
        repetitionFragment = new TwoLinesDayOfTheWeek();
        String title = getString(R.string.repeat);
        repetitionFragment.setTitle(title);
    }

    private void setSnoozeDurationFragment() {
        snoozeDurationFragment = new TwoLinesRadioGroup();
        String title = getString(R.string.snooze_duration);
        snoozeDurationFragment.setTitle(title);
        Bundle args = new Bundle();
        String[] snoozeDurations = getResources().getStringArray(R.array.snooze_durations);
        args.putStringArray(TwoLinesRadioGroup.ARG_OPTIONS_TEXT, snoozeDurations);
        long[] values = SnoozeDuration.getValues();
        args.putLongArray(TwoLinesRadioGroup.ARG_OPTIONS_VALUE, values);
        snoozeDurationFragment.setArguments(args);
    }

    private void setRingtoneFragment() {
        ringtoneFragment = new TwoLinesRingtone();
        String title = getString(R.string.ringtone);
        ringtoneFragment.setTitle(title);
    }

    private void setVolumeFragment() {
        volumeFragment = new TwoLinesSeekBar();
        String title = getString(R.string.volume);
        volumeFragment.setTitle(title);
    }

    private void setVibrationFragment() {
        vibrationFragment = new TwoLinesSwitch();
        String title = getString(R.string.vibrate);
        vibrationFragment.setTitle(title);
    }

    private void setSleepCheckerFragment() {
        sleepCheckerFragment = new TwoLinesSwitch();
        String title = getString(R.string.enable_sleep_checker);
        String summary = getString(R.string.summary_enable_sleep_checker);
        sleepCheckerFragment.setTitle(title);
        sleepCheckerFragment.setSummary(summary);
    }

    private void setTextFragment() {
        textFragment = new TwoLinesMemo();
        String title = getString(R.string.text);
        textFragment.setTitle(title);
    }

    private Alarm buildAlarm() {
        String title = titleFragment.getValue();
        long triggerTime = timePickerFragment.getValue().toCalendar().getTimeInMillis();
        SparseBooleanArray sparseBooleanArray = repetitionFragment.getValue();
        int[] repetition = Utils.convertSparseBooleanArrayToIntArray(sparseBooleanArray);
        SnoozeDuration snoozeDuration = SnoozeDuration.valueOf(snoozeDurationFragment.getValue());
        Uri ringtoneUri = ringtoneFragment.getValue();
        int volume = volumeFragment.getValue();
        boolean vibrate = vibrationFragment.getValue();
        boolean sleepCheckerOn = sleepCheckerFragment.getValue();
        String text = textFragment.getValue();
        AlarmBuilder builder = new AlarmBuilder().setId(alarmId)
                                                 .setTitle(title)
                                                 .setTriggerTime(triggerTime)
                                                 .setRepetition(repetition)
                                                 .setSnoozeDuration(snoozeDuration)
                                                 .setRingtoneUri(ringtoneUri)
                                                 .setVolume(volume)
                                                 .setVibrate(vibrate)
                                                 .setSleepCheckerOn(sleepCheckerOn)
                                                 .setText(text);
        return builder.build();
    }

    private void setFragmentValues() {
        Alarm alarm = dao.select(alarmId);

        titleFragment.setSummary(alarm.getTitle());
        titleFragment.setValue(alarm.getTitle());
        setTitle(alarm.getTitle());

        Time time = Time.valueOf(alarm.getTriggerTime());
        timePickerFragment.setSummary(time.toString());
        timePickerFragment.setValue(time);

        repetitionFragment.setValue(Utils.convertIntArrayToSparseBooleanArray(alarm.getRepetition()));

        snoozeDurationFragment.setSummary(alarm.getSnoozeDuration().toString());
        snoozeDurationFragment.setValue(alarm.getSnoozeDuration().getValue());

        RingtoneManager manager = new RingtoneManager(context);
        int ringtonePosition = manager.getRingtonePosition(alarm.getRingtoneUri());
        Ringtone ringtone = manager.getRingtone(ringtonePosition);
        ringtoneFragment.setSummary(ringtone.getTitle(context));
        ringtoneFragment.setValue(alarm.getRingtoneUri());

        volumeFragment.setValue(alarm.getVolume());

        vibrationFragment.setValue(alarm.vibrates());

        sleepCheckerFragment.setValue(alarm.isSleepCheckerOn());

        textFragment.setSummary(alarm.getText());
        textFragment.setValue(alarm.getText());
    }

    @Override
    public void onClick(View view) {
        final Alarm alarm = buildAlarm();
        alarmHandler = new AlarmHandler(context, alarm);
        boolean success;
        if (editMode)
            success = dao.update(alarm);
        else
            success = dao.insert(alarm);
        if (!success) {
            Snackbar.make(view, R.string.error_save_alarm, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean success;
                            if (editMode) {
                                success = dao.update(alarm);
                                if (success)
                                    alarmHandler.updateAlarm();
                            } else {
                                success = dao.insert(alarm);
                                if (success)
                                    alarmHandler.setAlarm();
                            }
                            if (success)
                                Toast.makeText(context, R.string.alarm_saved, Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        } else {
            if (editMode)
                alarmHandler.updateAlarm();
            else
                alarmHandler.setAlarm();
            Toast.makeText(context, R.string.alarm_saved, Toast.LENGTH_SHORT)
                 .show();
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }
    }

}
