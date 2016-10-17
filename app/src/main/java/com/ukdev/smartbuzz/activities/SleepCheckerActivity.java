package com.ukdev.smartbuzz.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.database.AlarmRepository;
import com.ukdev.smartbuzz.extras.AlarmHandler;
import com.ukdev.smartbuzz.extras.AppConstants;
import com.ukdev.smartbuzz.extras.FrontEndTools;
import com.ukdev.smartbuzz.model.Alarm;

/**
 * Sleep checker
 * Created by Alan Camargo - March 2016
 */
public class SleepCheckerActivity extends AppCompatActivity
{

    private CountDownTimer countdownTimer;
    private Alarm alarm;
    private PowerManager.WakeLock wakeLock;
    private AlarmRepository database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_checker);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        database = AlarmRepository.getInstance(this);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP, "Tag");
        if (FrontEndTools.screenIsLocked(this))
            wakeLock.acquire();
        alarm = database.select(this, getIntent()
                .getIntExtra(AppConstants.EXTRA_ID, AppConstants.DEFAULT_INTENT_EXTRA));
        TextView alarmTitle = (TextView)findViewById(R.id.alarmTitle_SleepChecker);
        alarmTitle.setText(alarm.getTitle());
        setYesButton();
        countdown();
    }

    @Override
    public void onBackPressed()
    {
        // Do nothing
    }

    /**
     * Sets an action to yesButton
     */
    private void setYesButton()
    {
        Button yesButton = (Button)findViewById(R.id.yesButton);
        yesButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        stopCountdown();
                    }
                }
        );
    }

    /**
     * Handles the ongoing countdownTimer
     */
    private void stopCountdown()
    {
        countdownTimer.cancel();
        alarm.setLocked(false);
        database.update(this, alarm.getId(), alarm);
        if (alarm != null && !alarm.repeats())
            AlarmHandler.killAlarm(getBaseContext(), alarm);
        if (wakeLock.isHeld())
            wakeLock.release();
        FrontEndTools.killApp(this);
    }

    /**
     * Counts 10s before triggering the alarm again
     */
    private void countdown()
    {
        final TextView countdownText = (TextView)findViewById(R.id.countdownText);
        countdownTimer = new CountDownTimer(AppConstants.FIFTEEN_SECONDS,
                AppConstants.ONE_SECOND)
        {
            @Override
            public void onTick(long millisecondsUntilFinished)
            {
                int timeLeft = (int)(millisecondsUntilFinished / 1000);
                switch (timeLeft)
                {
                    case 14:
                        countdownText.setTextColor(Color.parseColor("#009688")); // Green
                        break;
                    case 9:
                        countdownText.setTextColor(Color.parseColor("#FFC107")); // Amber
                        break;
                    case 4:
                        countdownText.setTextColor(Color.parseColor("#F44336")); // Red
                        break;
                }
                countdownText.setText(String.valueOf(timeLeft));
            }

            @Override
            public void onFinish()
            {
                if (wakeLock.isHeld())
                    wakeLock.release();
                Intent alarmIntent = new Intent(SleepCheckerActivity.this,
                        AlarmActivity.class);
                alarmIntent.setAction(AppConstants.ACTION_MAYHEM);
                if (alarm != null)
                    alarmIntent.putExtra(AppConstants.EXTRA_ID, alarm.getId());
                startActivity(alarmIntent);
            }
        };
        countdownTimer.start();
    }

}
