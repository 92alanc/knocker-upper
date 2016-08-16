package com.ukdev.smartbuzz.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.*;
import android.widget.*;
import com.ukdev.smartbuzz.extras.*;
import com.ukdev.smartbuzz.model.*;
import com.ukdev.smartbuzz.view.CustomTimePicker;
import com.ukdev.smartbuzz.database.AlarmDAO;
import com.ukdev.smartbuzz.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Alarm Creator
 * Created by Alan Camargo - March 2016
 */
public class AlarmCreatorActivity extends AppCompatActivity
{

    private CustomTimePicker timePicker;
    private EditText titleBox;
    private CollapsingToolbarLayout toolbarLayout;
    private CheckBox repetitionCheckBox, reminderCheckBox, vibrateCheckBox;
    private int idToEdit;
    private boolean isEditing;
    private Spinner timeZoneSpinner, ringtoneSpinner, snoozeSpinner;
    private SeekBar volumeSeekBar;
    private MediaPlayer player;
    private ToggleButton[] repetitionButtons;
    private AudioManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_creator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        reminderCheckBox = (CheckBox)findViewById(R.id.reminderCheckBox);
        setToolbarLayout();
        setSaveButton();
        setTimePicker();
        repetitionButtons = FrontEndTools.buildRepetitionButtons(this, this);
        setRepetitionCheckBox();
        setRingtoneSpinner();
        setTimeZoneSpinner();
        setSnoozeSpinner();
        setVolumeSeekBar();
        setRingtoneTestButton();
        setVibrateCheckBox();
        setCancelButton();
        isEditing = getIntent().getAction().equals(AppConstants.ACTION_EDIT_ALARM);
        if (isEditing)
        {
            idToEdit = getIntent().getIntExtra(AppConstants.EXTRA_EDIT, 0);
            loadAlarmDataToEdit();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        snoozeSpinner = (Spinner)findViewById(R.id.snoozeSpinner);
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
                        if (isEditing)
                        {
                            if (update())
                                FrontEndTools.startActivity(AlarmCreatorActivity.this,
                                                        HomeActivity.class);
                        }
                        else
                        {
                            if (save())
                                FrontEndTools.startActivity(AlarmCreatorActivity.this,
                                                            HomeActivity.class);
                        }
                    }
                }
                                     );
    }

    /**
     * Sets actions to timeZoneSpinner
     */
    private void setTimeZoneSpinner()
    {
        timeZoneSpinner = (Spinner)findViewById(R.id.timeZoneSpinner);
        FrontEndTools.adaptTimeZonePicker(this, timeZoneSpinner);
        TimeZoneWrapper[] timeZones = TimeZoneWrapper.getAllTimeZones(this);
        TimeWrapper localTimeZone = TimeZoneWrapper.getLocalTimeZoneOffset();
        for (int i = 0; i < timeZones.length; i++)
        {
            if (timeZones[i].getOffset().toString().equals(localTimeZone.toString()))
            {
                timeZoneSpinner.setSelection(i);
                break;
            }
        }
    }

    /**
     * Sets actions to ringtoneSpinner
     */
    private void setRingtoneSpinner()
    {
        ringtoneSpinner = (Spinner)findViewById(R.id.ringtoneSpinner);
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
        int volume = (BackEndTools.getMaxVolume(manager) * volumeSeekBar.getProgress()) / 100;
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
                    if (ringtone.isPlayable())
                    {
                        player = new MediaPlayer();
                        int v = (BackEndTools.getMaxVolume(manager) * volumeSeekBar.getProgress()) / 100;
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
                        ringtoneTestButton.setImageResource(R.drawable.stop);
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer)
                            {
                                manager.abandonAudioFocus(listener);
                                ringtoneTestButton.setImageResource(R.drawable.play);
                            }
                        });
                    }
                    else
                        FrontEndTools.showToast(getBaseContext(),
                                                String.format(getString(R.string.invalid_ringtone_file),
                                                              ringtone.getTitle()), Toast.LENGTH_LONG);
                }
                else
                {
                    manager.abandonAudioFocus(listener);
                    player.stop();
                    ringtoneTestButton.setImageResource(R.drawable.play);
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
        toolbarLayout.setTitle(getTitle());
        toolbarLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                FrontEndTools.hideKeyboard(AlarmCreatorActivity.this);
                return false;
            }
        });
        titleBox = (EditText)findViewById(R.id.titleBox);
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
                if (titleBox.getText().toString().equals(""))
                    toolbarLayout.setTitle(getString(R.string.new_alarm));
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
        String title = titleBox.getText().toString().equals("") ?
                       getResources().getString(R.string.new_alarm) :
                       titleBox.getText().toString();
        if (ringtoneSpinner.getSelectedItem() == null) // Something terribly wrong happened
            ringtoneSpinner.setSelection(0);
        RingtoneWrapper ringtone = (RingtoneWrapper)ringtoneSpinner.getSelectedItem();
        if (AlarmDAO.hasDuplicates(this, title)
            && !title.equalsIgnoreCase(getString(R.string.new_alarm)))
        {
            FrontEndTools.showToast(this,
                                    String.format(getString(R.string.alarm_already_stored), title),
                                    Toast.LENGTH_LONG);
            return false;
        }
        else if (!ringtone.isPlayable())
        {
            FrontEndTools.showToast(this,
                                    String.format(getString(R.string.invalid_ringtone_file),
                                                  ringtone.getTitle()), Toast.LENGTH_LONG);
            return false;
        }
        else
        {
            if (AlarmDAO.hasDuplicates(this, title)
                && title.equalsIgnoreCase(getString(R.string.new_alarm)))
                title = title + " " + (AlarmDAO.getNewAlarmCount(this) + 1);
            timePicker.clearFocus();
            int hours;
            int minutes;
            if (AppConstants.OS_VERSION >= Build.VERSION_CODES.M)
            {
                hours = timePicker.getHour();
                minutes = timePicker.getMinute();
            }
            else
            {
                hours = timePicker.getCurrentHour();
                minutes = timePicker.getCurrentMinute();
            }
            TimeWrapper triggerTime = new TimeWrapper(hours, minutes);
            boolean reminder = reminderCheckBox.isChecked();
            int snooze = getSnoozeSpinnerValue();

            int id = AlarmDAO.selectAll(this, AppConstants.ORDER_BY_ID).length + 1;
            TimeZoneWrapper timeZone = (TimeZoneWrapper)timeZoneSpinner.getSelectedItem();
            int volume;
            float vol = (BackEndTools.getMaxVolume(manager) * volumeSeekBar.getProgress()) / 100;
            if (vol == (int)vol)
                volume = (int)vol;
            else
                volume = (int)vol + 1;
            boolean vibrate = vibrateCheckBox.isChecked();
            GridLayout layout = (GridLayout)findViewById(R.id.repetitionLayout);
            int[] repetition = BackEndTools.getSelectedRepetition(layout);
            if (player != null && player.isPlaying())
                player.stop();

            Alarm alarm = new Alarm(id, title, triggerTime, ringtone, volume, vibrate,
                                    reminder, true, timeZone, repetition, snooze);
            AlarmDAO.insert(this, alarm);
            AlarmHandler.scheduleAlarm(this, alarm);
            FrontEndTools.showToast(getBaseContext(),
                                    getString(R.string.alarm_set),
                                    Toast.LENGTH_SHORT);
            return true;
        }
    }

    /**
     * Updates an alarm
     * @return true if the operation has been successful,
     * otherwise false
     */
    private boolean update()
    {
        Alarm alarm = AlarmDAO.selectAll(this, AppConstants.ORDER_BY_ID)[(idToEdit - 1)];
        String originalTitle = alarm.getTitle();
        String title = titleBox.getText().toString().equals("") ?
                       getResources().getString(R.string.new_alarm) :
                       titleBox.getText().toString();
        if (ringtoneSpinner.getSelectedItem() == null) // Something terribly wrong happened
            ringtoneSpinner.setSelection(0);
        RingtoneWrapper ringtone = (RingtoneWrapper)ringtoneSpinner.getSelectedItem();
        if (!title.equalsIgnoreCase(originalTitle) && AlarmDAO.hasDuplicates(this, title))
        {
            FrontEndTools.showToast(this, String.format(getString(R.string.alarm_already_stored), title),
                                    Toast.LENGTH_LONG);
            return false;
        }
        else if (!ringtone.isPlayable())
        {
            FrontEndTools.showToast(this, String.format(getString(R.string.invalid_ringtone_file),
                                                        ringtone.getTitle()), Toast.LENGTH_LONG);
            return false;
        }
        else
        {
            alarm.setTitle(title);
            int hours;
            int minutes;
            if (AppConstants.OS_VERSION >= Build.VERSION_CODES.M)
            {
                hours = timePicker.getHour();
                minutes = timePicker.getMinute();
            }
            else
            {
                hours = timePicker.getCurrentHour();
                minutes = timePicker.getCurrentMinute();
            }
            TimeWrapper time = new TimeWrapper(hours, minutes);
            alarm.setTriggerTime(time);
            CheckBox reminderCheckBox = (CheckBox)findViewById(R.id.reminderCheckBox);
            alarm.setAsReminder(reminderCheckBox.isChecked());
            TimeZoneWrapper timeZone = (TimeZoneWrapper)timeZoneSpinner.getSelectedItem();
            alarm.setTimeZone(timeZone);
            alarm.setRingtone(ringtone);
            int volume;
            float vol = (BackEndTools.getMaxVolume(manager) * volumeSeekBar.getProgress()) / 100;
            if (vol == (int)vol)
                volume = (int)vol;
            else
                volume = (int)vol + 1;
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

            AlarmDAO.update(this, alarm.getId(), alarm);
            AlarmHandler.updateAlarm(this, alarm);
            FrontEndTools.showToast(getBaseContext(),
                                    getString(R.string.alarm_updated),
                                    Toast.LENGTH_SHORT);
            return true;
        }
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
        repetitionCheckBox = (CheckBox) findViewById(R.id.repetitionCheckBox);
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
                    for (ToggleButton button : repetitionButtons)
                        button.setChecked(false);
                }
            }
        });
    }

    /**
     * Sets functions to volumeSeekBar
     */
    private void setVolumeSeekBar()
    {
        volumeSeekBar = (SeekBar)findViewById(R.id.volumeSeekBar);
        volumeSeekBar.setClickable(false);
        volumeSeekBar.setFocusable(false);
    }

    /**
     * Sets functions to vibrateCheckBox
     */
    private void setVibrateCheckBox()
    {
        vibrateCheckBox = (CheckBox)findViewById(R.id.vibrateCheckBox);
    }

    /**
     * Loads alarm data to edit
     */
    private void loadAlarmDataToEdit()
    {
        Alarm alarmToEdit = AlarmDAO.select(this, idToEdit);
        toolbarLayout =
                (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        titleBox = (EditText)findViewById(R.id.titleBox);
        reminderCheckBox = (CheckBox)findViewById(R.id.reminderCheckBox);
        if (AppConstants.OS_VERSION >= Build.VERSION_CODES.M)
        {
            timePicker.setHour(alarmToEdit.getTriggerTime().getHours());
            timePicker.setMinute(alarmToEdit.getTriggerTime().getMinutes());
        }
        else
        {
            timePicker.setCurrentHour(alarmToEdit.getTriggerTime().getHours());
            timePicker.setCurrentMinute(alarmToEdit.getTriggerTime().getMinutes());
        }
        titleBox.setText(alarmToEdit.getTitle());
        toolbarLayout.setTitle(alarmToEdit.getTitle() + "*");
        reminderCheckBox.setChecked(alarmToEdit.isReminder());
        vibrateCheckBox.setChecked(alarmToEdit.vibrates());
        int progress = (alarmToEdit.getVolume() * 100) / BackEndTools.getMaxVolume(manager);
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
        String[] timeZones = getResources().getStringArray(R.array.timezones);
        for (int i = 0; i < timeZones.length; i++)
        {
            if (timeZones[i].equals(alarmToEdit.getTimeZone().getTitle()))
            {
                timeZoneSpinner.setSelection(i);
                break;
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
