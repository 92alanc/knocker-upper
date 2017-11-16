package com.ukdev.smartbuzz.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.fragments.DismissFragment;
import com.ukdev.smartbuzz.fragments.SnoozeFragment;
import com.ukdev.smartbuzz.listeners.OnFragmentInflatedListener;
import com.ukdev.smartbuzz.misc.Action;
import com.ukdev.smartbuzz.misc.Extra;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.Time;
import com.ukdev.smartbuzz.util.AlarmHandler;
import com.ukdev.smartbuzz.util.PreferenceUtils;
import com.ukdev.smartbuzz.util.Utils;
import com.ukdev.smartbuzz.util.ViewUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * The activity where alarms and
 * Sleep Checker are triggered
 *
 * @author Alan Camargo
 */
public class AlarmActivity extends AppCompatActivity {

    private AppCompatActivity activity;
    private Alarm alarm;
    private AlarmDao dao;
    private AlarmHandler alarmHandler;
    private boolean hellMode;
    private boolean sleepCheckerMode;
    private CountDownTimer countDownTimer;
    private int alarmId;
    private TextView nameTextView;
    private PowerManager.WakeLock wakeLock;

    public static Intent getIntent(Context context, int alarmId, boolean triggerSleepChecker) {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.putExtra(Extra.ID, alarmId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (triggerSleepChecker)
            intent.setAction(Action.TRIGGER_SLEEP_CHECKER);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(PreferenceUtils.FILE_NAME,
                                                                   MODE_PRIVATE);
        PreferenceUtils preferenceUtils = new PreferenceUtils(this, sharedPreferences);
        setTheme(preferenceUtils.getTheme().getRes());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                                     WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                                     WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                                     WindowManager.LayoutParams.FLAG_FULLSCREEN |
                                     WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                                     WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        parseIntent();
        initialiseComponents();
    }

    @Override
    public void onBackPressed() { }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sleepCheckerMode) {
            stopCountdown();
            Utils.killApp(activity);
        } else {
            alarm.getMediaPlayer().release();
            if (alarm.vibrates() || hellMode)
                alarm.getVibrator().cancel();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recreate();
    }

    private void initialiseComponents() {
        activity = this;
        Context context = this;
        hellMode = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager == null)
            return;
        final int levelAndFlags = PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP;
        final String tag = "Tag";
        wakeLock = powerManager.newWakeLock(levelAndFlags, tag);
        if (ViewUtils.isScreenLocked(context))
            wakeLock.acquire(Time.ONE_MINUTE);
        dao = AlarmDao.getInstance(context);
        alarm = dao.select(alarmId);
        alarmHandler = new AlarmHandler(context, alarm);
        nameTextView = findViewById(R.id.text_view_alarm_name);
        if (!sleepCheckerMode) {
            nameTextView.setText(alarm.getName());
            alarm.playRingtone(activity, hellMode);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alarmHandler.dismissAlarm(activity, wakeLock);
                }
            }, Time.ONE_MINUTE);
        } else {
            nameTextView.setText(context.getText(R.string.are_you_awake));
            startCountdown();
        }
        setSnoozeButtonPlaceholder();
        setDismissFragment(sleepCheckerMode);
        Uri wallpaperUri = alarm.getWallpaperUri();
        if (wallpaperUri != null && !sleepCheckerMode) {
            RelativeLayout layout = findViewById(R.id.layout_alarm);
            try {
                InputStream inputStream = getContentResolver().openInputStream(wallpaperUri);
                Drawable background = Drawable.createFromStream(inputStream, wallpaperUri.toString());
                layout.setBackground(background);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseIntent() {
        alarmId = getIntent().getIntExtra(Extra.ID, -1);
        sleepCheckerMode = Action.TRIGGER_SLEEP_CHECKER.equals(getIntent().getAction());
    }

    private void raiseHell() {
        hellMode = true;
        final boolean sleepCheckerOn = false;
        setDismissFragment(sleepCheckerOn);
        nameTextView.setText(alarm.getName());
        if (wakeLock.isHeld())
            wakeLock.release();
        alarm.playRingtone(activity, hellMode);
    }

    private void setSnoozeButtonPlaceholder() {
        FrameLayout snoozeButtonPlaceholder = findViewById(R.id.placeholder_snooze_button);
        if (sleepCheckerMode || hellMode || alarm.getSnoozeDuration() == Time.ZERO)
            snoozeButtonPlaceholder.setVisibility(View.GONE);
        else {
            snoozeButtonPlaceholder.setVisibility(View.VISIBLE);
            setSnoozeFragment();
        }
    }

    private void setSnoozeFragment() {
        SnoozeFragment snoozeFragment = new SnoozeFragment();
        snoozeFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onFragmentInflated(Fragment fragment) {
                ((SnoozeFragment) fragment).setButtonText(alarm.getSnoozeDuration());
                ((SnoozeFragment) fragment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alarm.getMediaPlayer().release();
                        if (alarm.vibrates() || hellMode)
                            alarm.getVibrator().cancel();
                        alarmHandler.delayAlarm();
                        Utils.killApp(activity);
                    }
                });
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.placeholder_snooze_button, snoozeFragment);
        transaction.commit();
    }

    private void setDismissFragment(final boolean sleepCheckerOn) {
        DismissFragment dismissFragment = new DismissFragment();
        dismissFragment.setOnFragmentInflatedListener(new OnFragmentInflatedListener() {
            @Override
            public void onFragmentInflated(Fragment fragment) {
                ((DismissFragment) fragment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sleepCheckerOn) {
                            stopCountdown();
                            if (!alarm.repeats()) {
                                alarm.setActive(false);
                                dao.update(alarm);
                            }
                            Utils.killApp(activity);
                        } else {
                            alarm.getMediaPlayer().release();
                            if (alarm.vibrates() || hellMode)
                                alarm.getVibrator().cancel();
                            alarmHandler.dismissAlarm(activity, wakeLock);
                        }
                    }
                });
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.placeholder_dismiss_button, dismissFragment);
        transaction.commit();
    }

    private void startCountdown() {
        final TextView countdownTextView = findViewById(R.id.text_view_countdown);
        countdownTextView.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(Time.FIFTEEN_SECONDS, Time.ONE_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                Context context = getBaseContext();
                switch (secondsLeft) {
                    case 14:
                        countdownTextView.setTextColor(ContextCompat.getColor(context, R.color.green));
                        break;
                    case 9:
                        countdownTextView.setTextColor(ContextCompat.getColor(context, R.color.amber));
                        break;
                    case 4:
                        countdownTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
                        break;
                }
                countdownTextView.setText(String.valueOf(secondsLeft));
            }

            @Override
            public void onFinish() {
                countdownTextView.setVisibility(View.GONE);
                raiseHell();
            }
        };
        countDownTimer.start();
    }

    private void stopCountdown() {
        countDownTimer.cancel();
        if (wakeLock.isHeld())
            wakeLock.release();
    }

}
