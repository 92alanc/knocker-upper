package com.ukdev.smartbuzz.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.ukdev.smartbuzz.fragments.TwoLinesDayOfTheWeek;
import com.ukdev.smartbuzz.fragments.TwoLinesDefaultFragment;
import com.ukdev.smartbuzz.fragments.TwoLinesEditText;
import com.ukdev.smartbuzz.fragments.TwoLinesImagePicker;
import com.ukdev.smartbuzz.fragments.TwoLinesMemo;
import com.ukdev.smartbuzz.fragments.TwoLinesRadioGroup;
import com.ukdev.smartbuzz.fragments.TwoLinesRingtone;
import com.ukdev.smartbuzz.fragments.TwoLinesSeekBar;
import com.ukdev.smartbuzz.fragments.TwoLinesSwitch;
import com.ukdev.smartbuzz.fragments.TwoLinesTimePicker;
import com.ukdev.smartbuzz.listeners.OnFragmentInflatedListener;
import com.ukdev.smartbuzz.misc.Extra;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.AlarmBuilder;
import com.ukdev.smartbuzz.model.Time;
import com.ukdev.smartbuzz.util.AlarmHandler;
import com.ukdev.smartbuzz.util.Utils;
import com.ukdev.smartbuzz.util.ViewUtils;

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
    private TwoLinesEditText nameFragment;
    private TwoLinesTimePicker timePickerFragment;
    private TwoLinesDayOfTheWeek repetitionFragment;
    private TwoLinesRadioGroup snoozeDurationFragment;
    private TwoLinesRingtone ringtoneFragment;
    private TwoLinesSeekBar volumeFragment;
    private TwoLinesSwitch vibrationFragment;
    private TwoLinesSwitch sleepCheckerFragment;
    private TwoLinesMemo textFragment;
    private TwoLinesImagePicker wallpaperFragment;
    private View.OnClickListener onClickListener;

    public static Intent getIntent(Context context) {
        return new Intent(context, SetupActivity.class);
    }

    public static Intent getIntent(Context context, int alarmId) {
        Intent intent = new Intent(context, SetupActivity.class);
        intent.putExtra(Extra.ID, alarmId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        Activity activity = this;
        ViewUtils.showAds(activity);
        parseIntent();
        initialiseComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        replaceFragmentPlaceholders();
        if (editMode) {
            alarmHandler = new AlarmHandler(context, dao.select(alarmId));
            setFragmentValues();
            replaceFragmentPlaceholders();
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recreate();
    }

    @Override
    public void onClick(View view) {
        final Alarm alarm = buildAlarm();
        alarmHandler = new AlarmHandler(context, alarm);
        long id;
        boolean success;
        if (editMode)
            success = dao.update(alarm);
        else {
            id = dao.insert(alarm);
            alarm.setId((int) id);
            success = id > 0;
        }
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
                                long id = dao.insert(alarm);
                                alarm.setId((int) id);
                                success = id > 0;
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
        setWallpaperFragment();
    }

    private void delete() {
        final Alarm alarm = dao.select(alarmId);
        if (!dao.delete(alarm)) {
            View focus = getCurrentFocus();
            if (focus != null) {
                Snackbar snackbar = Snackbar.make(focus,
                                                  R.string.error_delete_alarm,
                                                  Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.error_delete_alarm, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dao.delete(alarm)) {
                            alarmHandler.cancelAlarm();
                            ackDelete();
                        }
                    }
                });
                snackbar.show();
            }
        } else {
            alarmHandler.cancelAlarm();
            ackDelete();
        }
    }

    private void ackDelete() {
        Toast.makeText(context, R.string.alarm_deleted, Toast.LENGTH_SHORT)
             .show();
    }

    private void parseIntent() {
        alarmId = getIntent().getIntExtra(Extra.ID, 0);
        editMode = alarmId > 0;
    }

    private void replaceFragmentPlaceholders() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.placeholder_name, nameFragment);
        transaction.replace(R.id.placeholder_time_picker, timePickerFragment);
        transaction.replace(R.id.placeholder_repetition, repetitionFragment);
        transaction.replace(R.id.placeholder_snooze_duration, snoozeDurationFragment);
        transaction.replace(R.id.placeholder_ringtone, ringtoneFragment);
        transaction.replace(R.id.placeholder_volume, volumeFragment);
        transaction.replace(R.id.placeholder_vibrate, vibrationFragment);
        transaction.replace(R.id.placeholder_sleep_checker, sleepCheckerFragment);
        transaction.replace(R.id.placeholder_text, textFragment);
        transaction.replace(R.id.placeholder_wallpaper, wallpaperFragment);
        transaction.commit();
    }

    private void setSaveButton() {
        FloatingActionButton saveButton = findViewById(R.id.fab_setup);
        saveButton.setOnClickListener(onClickListener);
    }

    private void setTitleFragment() {
        nameFragment = new TwoLinesEditText();
        String name = getString(R.string.name);
        nameFragment.setTitle(name);
        nameFragment.setChangeListener(new TwoLinesDefaultFragment.TwoLinesChangeListener<String>() {
            @Override
            public void onChange(String newValue) {
                setTitle(newValue);
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
        long[] values = { Time.ZERO, Time.FIVE_MINUTES, Time.TEN_MINUTES,
                          Time.FIFTEEN_MINUTES, Time.TWENTY_MINUTES,
                          Time.TWENTY_FIVE_MINUTES, Time.THIRTY_MINUTES };
        args.putLongArray(TwoLinesRadioGroup.ARG_OPTIONS_VALUE, values);
        snoozeDurationFragment.setArguments(args);
        snoozeDurationFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
                snoozeDurationFragment.setDefaultSelectedItem();
            }
        });
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
        vibrationFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
                ((TwoLinesSwitch) fragment).setValue(true);
            }
        });
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

    private void setWallpaperFragment() {
        wallpaperFragment = new TwoLinesImagePicker();
        String title = getString(R.string.wallpaper);
        wallpaperFragment.setTitle(title);
    }

    private Alarm buildAlarm() {
        String name = nameFragment.getValue() != null ? nameFragment.getValue() : getString(R.string.new_alarm);
        long triggerTime = timePickerFragment.getValue().toCalendar().getTimeInMillis();
        SparseBooleanArray sparseBooleanArray = repetitionFragment.getValue();
        Integer[] repetition = Utils.convertSparseBooleanArrayToIntArray(sparseBooleanArray);
        long snoozeDuration = snoozeDurationFragment.getValue();
        Uri ringtoneUri = ringtoneFragment.getValue();
        Uri wallpaperUri = wallpaperFragment.getValue();
        int volume = volumeFragment.getValue();
        boolean vibrate = vibrationFragment.getValue();
        boolean sleepCheckerOn = sleepCheckerFragment.getValue();
        String text = textFragment.getValue();
        AlarmBuilder builder = new AlarmBuilder().setId(alarmId)
                                                 .setName(name)
                                                 .setTriggerTime(triggerTime)
                                                 .setRepetition(repetition)
                                                 .setSnoozeDuration(snoozeDuration)
                                                 .setRingtoneUri(ringtoneUri)
                                                 .setVolume(volume)
                                                 .setVibrate(vibrate)
                                                 .setSleepCheckerOn(sleepCheckerOn)
                                                 .setText(text)
                                                 .setWallpaperUri(wallpaperUri);
        return builder.build();
    }

    private void setFragmentValues() {
        final Alarm alarm = dao.select(alarmId);

        if (alarm == null)
            return;

        nameFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
                nameFragment.setSummary(alarm.getName());
                nameFragment.setValue(alarm.getName());
            }
        });
        setTitle(alarm.getName());

        timePickerFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
                Time time = Time.valueOf(alarm.getTriggerTime());
                timePickerFragment.setSummary(time.toString());
                timePickerFragment.setValue(time);
            }
        });

        repetitionFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
                repetitionFragment.setValue(Utils.convertIntArrayToSparseBooleanArray(alarm.getRepetition()));
            }
        });

        snoozeDurationFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
                String summary = Utils.convertSnoozeDurationToString(context,
                                                                     alarm.getSnoozeDuration());
                snoozeDurationFragment.setSummary(summary);
                snoozeDurationFragment.setValue(alarm.getSnoozeDuration());
            }
        });

        ringtoneFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
                RingtoneManager manager = new RingtoneManager(context);
                int ringtonePosition = manager.getRingtonePosition(alarm.getRingtoneUri());
                if (alarm.getRingtoneUri() == null)
                    ringtoneFragment.setSummary(getString(R.string.ringtone_none));
                else {
                    Ringtone ringtone = manager.getRingtone(ringtonePosition);
                    ringtoneFragment.setSummary(ringtone.getTitle(context));
                    ringtoneFragment.setValue(alarm.getRingtoneUri());
                }
            }
        });

        volumeFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
                volumeFragment.setValue(alarm.getVolume());
            }
        });

        vibrationFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
                vibrationFragment.setValue(alarm.vibrates());
            }
        });

        sleepCheckerFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
                sleepCheckerFragment.setValue(alarm.isSleepCheckerOn());
            }
        });

        textFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
                textFragment.setSummary(alarm.getText());
                textFragment.setValue(alarm.getText());
            }
        });

        wallpaperFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
                Uri wallpaperUri = alarm.getWallpaperUri();
                if (wallpaperUri != null)
                    wallpaperFragment.setValue(wallpaperUri);
            }
        });
    }

}
