package com.ukdev.smartbuzz.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.ukdev.smartbuzz.database.AlarmRepository;
import com.ukdev.smartbuzz.database.SnoozeCounter;
import com.ukdev.smartbuzz.extras.AlarmHandler;
import com.ukdev.smartbuzz.extras.AppConstants;
import com.ukdev.smartbuzz.extras.FrontEndTools;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.R;

/**
 * Alarm activity
 * Created by Alan Camargo - March 2016
 */
public class AlarmActivity extends AppCompatActivity
{

    private Alarm alarm;
    private PowerManager.WakeLock wakeLock;
    private final SnoozeCounter snoozeCounter = new SnoozeCounter(this);
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP, "Tag");
        if (FrontEndTools.screenIsLocked(this))
            wakeLock.acquire();
        alarm = AlarmRepository.getInstance(this).select(this, getIntent().getIntExtra(AppConstants.EXTRA_ID,
                AppConstants.DEFAULT_INTENT_EXTRA));
        setTitle();
        countdown();
        setDismissButton();
        setSnoozeButton();
        alarm.playRingtone(AlarmActivity.this, getBaseContext());
    }

    @Override
    public void onBackPressed()
    {
        // Do nothing
    }

    /**
     * Sets the title
     */
    private void setTitle()
    {
        TextView title = (TextView)findViewById(R.id.alarmTitle);
        title.setText(alarm.getTitle());
    }

    /**
     * Sets actions to dismissButton
     */
    private void setDismissButton()
    {
        Button dismissButton = (Button) findViewById(
                R.id.dismissButton);
        dismissButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                timer.cancel();
                snoozeCounter.reset();
                AlarmHandler.dismissAlarm(alarm, AlarmActivity.this,
                        getBaseContext(), alarm.getPlayer(), alarm.getVibrator(), wakeLock);
            }
        });
    }

    /**
     * Sets actions to snoozeButton
     */
    private void setSnoozeButton()
    {
        Button snoozeButton = (Button) findViewById(R.id.snoozeButton);
        if (getIntent().getAction().equals(AppConstants.ACTION_MAYHEM)
                || snoozeCounter.getCount() == 3)
            snoozeButton.setVisibility(View.GONE);
        else
        {
            snoozeButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    timer.cancel();
                    if (wakeLock.isHeld())
                        wakeLock.release();
                    if (alarm.vibrates() || getIntent().getAction().equals(AppConstants.ACTION_MAYHEM))
                        alarm.getVibrator().cancel();
                    alarm.getPlayer().release();
                    snoozeCounter.update(snoozeCounter.getCount() + 1);
                    AlarmHandler.snoozeAlarm(getBaseContext(), alarm);
                    FrontEndTools.killApp(AlarmActivity.this);
                }
            });
        }
    }

    /**
     * Counts down 1 minute until the alarm dismisses itself
     */
    private void countdown()
    {
        timer = new CountDownTimer(AppConstants.ONE_MINUTE, AppConstants.ONE_SECOND)
        {
            @Override
            public void onTick(long l)
            {
                // Do nothing
            }

            @Override
            public void onFinish()
            {
                AlarmHandler.dismissAlarm(alarm, AlarmActivity.this, getBaseContext(),
                                          alarm.getPlayer(), alarm.getVibrator(), wakeLock);
            }
        };
        timer.start();
    }

}
