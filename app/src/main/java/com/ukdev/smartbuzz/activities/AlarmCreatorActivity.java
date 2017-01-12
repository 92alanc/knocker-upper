package com.ukdev.smartbuzz.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.*;
import android.widget.*;
import com.ukdev.smartbuzz.backend.AlarmHandler;
import com.ukdev.smartbuzz.backend.AudioFocusChangeListener;
import com.ukdev.smartbuzz.backend.BackEndTools;
import com.ukdev.smartbuzz.extras.*;
import com.ukdev.smartbuzz.frontend.FrontEndTools;
import com.ukdev.smartbuzz.model.*;
import com.ukdev.smartbuzz.frontend.CustomTimePicker;
import com.ukdev.smartbuzz.database.AlarmRepository;
import com.ukdev.smartbuzz.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Alarm Creator
 * Created by Alan Camargo - March 2016
 */
public class AlarmCreatorActivity extends AppCompatActivity
{

    private CustomTimePicker timePicker;
    private AppCompatEditText titleBox;
    private CollapsingToolbarLayout toolbarLayout;
    private AppCompatCheckBox repetitionCheckBox, reminderCheckBox, vibrateCheckBox;
    private int idToEdit;
    private boolean isEditing, isReminder;
    private AppCompatSpinner ringtoneSpinner, snoozeSpinner;
    private AppCompatSeekBar volumeSeekBar;
    private MediaPlayer player;
    private AudioManager manager;
    private AlarmRepository database;
    private Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_creator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = AlarmRepository.getInstance(this);
        manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        reminderCheckBox = (AppCompatCheckBox)findViewById(R.id.reminderCheckBox);
        isReminder = getIntent().getBooleanExtra(AppConstants.EXTRA_REMINDER, false);
        setToolbarLayout();
        setSaveButton();
        setTimePicker();
        setRepetitionCheckBox();
        setSundayButton();
        setMondayButton();
        setTuesdayButton();
        setWednesdayButton();
        setThursdayButton();
        setFridayButton();
        setSaturdayButton();
        setRingtoneSpinner();
        setSnoozeSpinner();
        setVolumeSeekBar();
        setRingtoneTestButton();
        setVibrateCheckBox();
        setCancelButton();
        isEditing = getIntent().getAction().equals(AppConstants.ACTION_EDIT_ALARM);
        if (isEditing)
        {
            reminderCheckBox.setVisibility(View.VISIBLE);
            idToEdit = getIntent().getIntExtra(AppConstants.EXTRA_EDIT, 0);
            loadAlarmDataToEdit();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        FrontEndTools.showAds(this, R.id.alarmCreatorAdView);
    }

    @Override
    public void onBackPressed()
    {
        if (player != null && player.isPlaying())
            player.stop();
        FrontEndTools.showToast(this, getString(R.string.cancelled), Toast.LENGTH_SHORT);
        Intent intent = new Intent(AlarmCreatorActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    /**
     * Sets actions to snoozeSpinner
     */
    private void setSnoozeSpinner()
    {
        snoozeSpinner = (AppCompatSpinner)findViewById(R.id.snoozeSpinner);
        FrontEndTools.adaptSnoozeSpinner(this, snoozeSpinner);
    }

    /**
     * Sets actions to saveButton
     */
    private void setSaveButton()
    {
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        boolean success = false;
                        if (isEditing)
                        {
                            if (update())
                            {
                                success = true;
                                FrontEndTools.startActivity(AlarmCreatorActivity.this,
                                        HomeActivity.class);
                            }
                        }
                        else
                        {
                            if (save())
                            {
                                success = true;
                                FrontEndTools.startActivity(AlarmCreatorActivity.this,
                                        HomeActivity.class);
                            }
                        }
                        if (success)
                            FrontEndTools.showTimeLeftToTrigger(getBaseContext(), alarm);
                    }
                }
        );
    }

    /**
     * Sets actions to ringtoneSpinner
     */
    private void setRingtoneSpinner()
    {
        ringtoneSpinner = (AppCompatSpinner)findViewById(R.id.ringtoneSpinner);
        FrontEndTools.adaptRingtonePicker(this, ringtoneSpinner);
    }

    /**
     * Gets the selected value at snoozeSpinner
     * @return value
     */
    private int getSnoozeSpinnerValue()
    {
        int selectedPosition = snoozeSpinner.getSelectedItemPosition();
        int snooze;
        switch (selectedPosition)
        {
            case 0:
                snooze = 5;
                break;
            case 1:
                snooze = 10;
                break;
            case 2:
                snooze = 15;
                break;
            default:
                snooze = 0;
                break;
        }
        return snooze;
    }

    /**
     * Sets actions to ringtoneTestButton
     */
    private void setRingtoneTestButton()
    {
        int volume = volumeSeekBar.getProgress();
        final AudioFocusChangeListener listener = new AudioFocusChangeListener(manager,
                volume);
        final ImageButton ringtoneTestButton = (ImageButton)findViewById(
                R.id.ringtoneTestButton);
        ringtoneTestButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (player == null || !player.isPlaying())
                {
                    RingtoneWrapper ringtone = (RingtoneWrapper)
                            ringtoneSpinner.getSelectedItem();
                    player = new MediaPlayer();
                    int v = volumeSeekBar.getProgress();
                    manager.setStreamVolume(AudioManager.STREAM_ALARM, v, 0);
                    int requestResult;
                    if (AppConstants.OS_VERSION >= Build.VERSION_CODES.KITKAT)
                        requestResult = manager.requestAudioFocus(listener,
                                AudioManager.STREAM_ALARM,
                                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
                    else
                        requestResult = manager.requestAudioFocus(listener,
                                AudioManager.STREAM_ALARM,
                                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                    if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                    {
                        player.setAudioStreamType(AudioManager.STREAM_ALARM);
                        try
                        {
                            player.setDataSource(getBaseContext(), ringtone.getUri());
                            player.prepare();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
                    {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
                        {
                            manager.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
                            if (!player.isPlaying())
                                player.start();
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar)
                        {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar)
                        {

                        }
                    });
                    player.start();
                    ringtoneTestButton.setImageResource(R.mipmap.ic_stop_black_36dp);
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                    {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer)
                        {
                            manager.abandonAudioFocus(listener);
                            ringtoneTestButton.setImageResource(R.mipmap.ic_play_arrow_black_36dp);
                        }
                    });
                }
                else
                {
                    manager.abandonAudioFocus(listener);
                    player.stop();
                    ringtoneTestButton.setImageResource(R.mipmap.ic_play_arrow_black_36dp);
                }
            }
        });
    }

    /**
     * Sets functions to toolbarLayout
     */
    private void setToolbarLayout()
    {
        toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        if (isReminder)
            toolbarLayout.setTitle(getString(R.string.new_reminder));
        else
            toolbarLayout.setTitle(getString(R.string.new_alarm));
        toolbarLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                FrontEndTools.hideKeyboard(AlarmCreatorActivity.this);
                return false;
            }
        });
        titleBox = (AppCompatEditText)findViewById(R.id.titleBox);
        titleBox.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence,
                                          int i, int i1, int i2)
            {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence,
                                      int i, int i1, int i2)
            {
                toolbarLayout.setTitle(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (isReminder)
                {
                    if (titleBox.getText().toString().equals(""))
                        toolbarLayout.setTitle(getString(R.string.new_reminder));
                }
                else
                {
                    if (titleBox.getText().toString().equals(""))
                        toolbarLayout.setTitle(getString(R.string.new_alarm));
                }
            }
        });
    }

    /**
     * Saves the alarm
     * @return true if the operation has been successful,
     * otherwise false
     */
    private boolean save()
    {
        String title;
        if (isReminder)
            title = titleBox.getText().toString().equals("") ?
                    getString(R.string.new_reminder) :
                    titleBox.getText().toString();
        else
            title = titleBox.getText().toString().equals("") ?
                    getResources().getString(R.string.new_alarm) :
                    titleBox.getText().toString();
        if (ringtoneSpinner.getSelectedItem() == null) // Something terribly wrong happened
            ringtoneSpinner.setSelection(0);
        RingtoneWrapper ringtone = (RingtoneWrapper)ringtoneSpinner.getSelectedItem();
        int id = database.getLastId() + 1;
        timePicker.clearFocus();
        int hours, minutes;
        hours = getHours();
        minutes = getMinutes();
        Calendar triggerTime = Calendar.getInstance();
        triggerTime.set(Calendar.HOUR_OF_DAY, hours);
        triggerTime.set(Calendar.MINUTE, minutes);
        int snooze = getSnoozeSpinnerValue();

        int volume = volumeSeekBar.getProgress();
        boolean vibrate = vibrateCheckBox.isChecked();
        GridLayout layout = (GridLayout)findViewById(R.id.repetitionLayout);
        int[] repetition = BackEndTools.getSelectedRepetition(layout);
        if (player != null && player.isPlaying())
            player.stop();

        alarm = new Alarm(id, title, triggerTime, ringtone, volume, vibrate,
                isReminder, true, repetition, snooze);
        database.insert(alarm);
        AlarmHandler.scheduleAlarm(this, alarm);
        return true;
    }

    /**
     * Updates an alarm
     * @return true if the operation has been successful,
     * otherwise false
     */
    private boolean update()
    {
        alarm = database.select(idToEdit);
        String title;
        if (isReminder)
            title = titleBox.getText().toString().equals("") ?
                    getResources().getString(R.string.new_reminder) :
                    titleBox.getText().toString();
        else
            title = titleBox.getText().toString().equals("") ?
                    getResources().getString(R.string.new_alarm) :
                    titleBox.getText().toString();
        if (ringtoneSpinner.getSelectedItem() == null) // Something terribly wrong happened
            ringtoneSpinner.setSelection(0);
        RingtoneWrapper ringtone = (RingtoneWrapper)ringtoneSpinner.getSelectedItem();
        alarm.setTitle(title);
        int hours, minutes;
        hours = getHours();
        minutes = getMinutes();
        Calendar triggerTime = Calendar.getInstance();
        triggerTime.set(Calendar.HOUR_OF_DAY, hours);
        triggerTime.set(Calendar.MINUTE, minutes);
        alarm.setTriggerTime(triggerTime);
        AppCompatCheckBox reminderCheckBox = (AppCompatCheckBox)findViewById(R.id.reminderCheckBox);
        alarm.setAsReminder(reminderCheckBox.isChecked());
        alarm.setRingtone(ringtone);
        int volume = volumeSeekBar.getProgress();
        alarm.setVolume(volume);
        boolean vibrate = vibrateCheckBox.isChecked();
        alarm.toggleVibration(vibrate);
        int snooze = getSnoozeSpinnerValue();
        alarm.setSnooze(snooze);
        GridLayout layout = (GridLayout)findViewById(R.id.repetitionLayout);
        int[] repetition = BackEndTools.getSelectedRepetition(layout);
        alarm.setRepetition(repetition);
        alarm.toggle(true);
        if (player != null && player.isPlaying())
            player.stop();

        database.update(alarm.getId(), alarm);
        AlarmHandler.updateAlarm(this, alarm);
        return true;
    }

    private int getHours()
    {
        int hours;
        if (AppConstants.OS_VERSION >= Build.VERSION_CODES.M)
            hours = timePicker.getHour();
        else
            hours = timePicker.getCurrentHour();
        return hours;
    }

    private int getMinutes()
    {
        int minutes;
        if (AppConstants.OS_VERSION >= Build.VERSION_CODES.M)
            minutes = timePicker.getMinute();
        else
            minutes = timePicker.getCurrentMinute();
        return minutes;
    }

    /**
     * Sets functions to timePicker
     */
    private void setTimePicker()
    {
        timePicker = (CustomTimePicker)findViewById(R.id.timePicker);
        if (DateFormat.is24HourFormat(getBaseContext()))
            timePicker.setIs24HourView(true);
        timePicker.setHapticFeedbackEnabled(true);
        timePicker.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                FrontEndTools.hideKeyboard(AlarmCreatorActivity.this);
                return false;
            }
        });
    }

    /**
     * Sets functions to repetitionCheckBox
     */
    private void setRepetitionCheckBox()
    {
        repetitionCheckBox = (AppCompatCheckBox) findViewById(R.id.repetitionCheckBox);
        final HorizontalScrollView scrollView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView);
        final ImageView imageView = (ImageView)findViewById(R.id.repetitionIcon);
        repetitionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if (isChecked)
                {
                    scrollView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
                else
                {
                    scrollView.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void handleToggleButton(int resId)
    {
        ToggleButton button = (ToggleButton) findViewById(resId);
        if (button.isChecked())
        {
            button.setBackgroundColor(Color.parseColor("#EF9A9A")); // Light red
            button.setTextColor(Color.parseColor("#FFFFFF")); // White
        }
        else
        {
            button.setBackgroundColor(Color.parseColor("#FFFFFF")); // White
            button.setTextColor(Color.parseColor("#000000")); // Black
        }
        button.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean isChecked)
                    {
                        compoundButton.setChecked(isChecked);
                        if (isChecked)
                        {
                            compoundButton.setBackgroundColor(Color.parseColor("#EF9A9A")); // Light red
                            compoundButton.setTextColor(Color.parseColor("#FFFFFF")); // White
                        }
                        else
                        {
                            compoundButton.setBackgroundColor(Color.parseColor("#FFFFFF")); // White
                            compoundButton.setTextColor(Color.parseColor("#000000")); // Black
                        }
                    }
                });
    }

    /**
     * Sets actions to sundayButton
     */
    private void setSundayButton()
    {
        handleToggleButton(R.id.sundayButton);
    }

    /**
     * Sets actions to mondayButton
     */
    private void setMondayButton()
    {
        handleToggleButton(R.id.mondayButton);
    }

    /**
     * Sets actions to tuesdayButton
     */
    private void setTuesdayButton()
    {
        handleToggleButton(R.id.tuesdayButton);
    }

    /**
     * Sets actions to wednesdayButton
     */
    private void setWednesdayButton()
    {
        handleToggleButton(R.id.wednesdayButton);
    }

    /**
     * Sets actions to thursdayButton
     */
    private void setThursdayButton()
    {
        handleToggleButton(R.id.thursdayButton);
    }

    /**
     * Sets actions to fridayButton
     */
    private void setFridayButton()
    {
        handleToggleButton(R.id.fridayButton);
    }

    /**
     * Sets actions to saturdayButton
     */
    private void setSaturdayButton()
    {
        handleToggleButton(R.id.saturdayButton);
    }

    /**
     * Sets functions to volumeSeekBar
     */
    private void setVolumeSeekBar()
    {
        volumeSeekBar = (AppCompatSeekBar)findViewById(R.id.volumeSeekBar);
        int limit = BackEndTools.getMaxVolume(manager);
        int progress = limit / 2;
        volumeSeekBar.setProgress(progress);
        volumeSeekBar.setMax(limit);
        volumeSeekBar.setClickable(false);
        volumeSeekBar.setFocusable(false);
    }

    /**
     * Sets functions to vibrateCheckBox
     */
    private void setVibrateCheckBox()
    {
        vibrateCheckBox = (AppCompatCheckBox)findViewById(R.id.vibrateCheckBox);
    }

    /**
     * Loads alarm data to edit
     */
    private void loadAlarmDataToEdit()
    {
        Alarm alarmToEdit = database.select(idToEdit);
        toolbarLayout =
                (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        titleBox = (AppCompatEditText)findViewById(R.id.titleBox);
        if (AppConstants.OS_VERSION >= Build.VERSION_CODES.M)
        {
            timePicker.setHour(alarmToEdit.getTriggerTime().get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(alarmToEdit.getTriggerTime().get(Calendar.MINUTE));
        }
        else
        {
            timePicker.setCurrentHour(alarmToEdit.getTriggerTime().get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(alarmToEdit.getTriggerTime().get(Calendar.MINUTE));
        }
        titleBox.setText(alarmToEdit.getTitle());
        toolbarLayout.setTitle(alarmToEdit.getTitle() + "*");
        reminderCheckBox.setChecked(alarmToEdit.isReminder());
        vibrateCheckBox.setChecked(alarmToEdit.vibrates());
        int progress = (alarmToEdit.getVolume());
        volumeSeekBar.setProgress(progress);
        ToggleButton[] repetitionButtons = FrontEndTools.getToggleButtons(
                (GridLayout)findViewById(R.id.repetitionLayout));
        int snooze = alarmToEdit.getSnooze();
        switch (snooze)
        {
            case 5:
                snoozeSpinner.setSelection(0);
                break;
            case 10:
                snoozeSpinner.setSelection(1);
                break;
            case 15:
                snoozeSpinner.setSelection(2);
                break;
            default:
                snoozeSpinner.setSelection(0);
                break;
        }
        if (alarmToEdit.repeats())
        {
            repetitionCheckBox.setChecked(true);
            int[] days = alarmToEdit.getRepetition();
            if (repetitionButtons != null && days != null)
            {
                for (int i = 0; i < repetitionButtons.length; i++)
                {
                    for (int day : days)
                    {
                        if (day - 1 == i)
                            repetitionButtons[i].setChecked(true);
                    }
                }
            }
        }
        ArrayList<RingtoneWrapper> ringtones = RingtoneWrapper.getAllRingtones(this);
        for (int i = 0; i < ringtones.size(); i++)
        {
            if (ringtones.get(i).getUri().equals(alarmToEdit.getRingtone().getUri()))
            {
                ringtoneSpinner.setSelection(i);
                break;
            }
        }
    }

    /**
     * Sets functions to cancelButton
     */
    private void setCancelButton()
    {
        Button cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if (player != null && player.isPlaying())
                            player.stop();
                        FrontEndTools.showToast(getBaseContext(),
                                getString(R.string.cancelled), Toast.LENGTH_SHORT);
                        Intent intent = new Intent(AlarmCreatorActivity.this,
                                HomeActivity.class);
                        AlarmCreatorActivity.this.startActivity(intent);
                    }
                }
        );
    }

}
