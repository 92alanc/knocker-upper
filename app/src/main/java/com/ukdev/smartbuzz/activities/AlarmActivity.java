package com.ukdev.smartbuzz.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.ukdev.smartbuzz.listeners.OnViewInflatedListener;
import com.ukdev.smartbuzz.misc.IntentAction;
import com.ukdev.smartbuzz.misc.IntentExtra;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.Time;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;
import com.ukdev.smartbuzz.system.AlarmHandler;
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
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                                     WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                                     WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                                     WindowManager.LayoutParams.FLAG_FULLSCREEN |
                                     WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                                     WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
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

    private void initialiseComponents() {
        activity = this;
        Context context = this;
        hellMode = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        final int levelAndFlags = PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP;
        final String tag = "Tag";
        wakeLock = powerManager.newWakeLock(levelAndFlags, tag);
        if (ViewUtils.isScreenLocked(context))
            wakeLock.acquire();
        String sleepCheckerAction = IntentAction.TRIGGER_SLEEP_CHECKER.toString();
        sleepCheckerMode = getIntent().getAction().equals(sleepCheckerAction);
        dao = AlarmDao.getInstance(context);
        int alarmId = getIntent().getIntExtra(IntentExtra.ID.toString(), 0);
        alarm = dao.select(alarmId);
        alarmHandler = new AlarmHandler(context, alarm);
        TextView titleTextView = findViewById(R.id.text_view_alarm_title);
        titleTextView.setText(alarm.getTitle());
        if (!sleepCheckerMode) {
            alarm.playRingtone(activity, hellMode);
            TextView text = findViewById(R.id.text_view_alarm_text);
            if (sleepCheckerMode)
                text.setText(R.string.are_you_awake);
            else
                text.setText(alarm.getText());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alarmHandler.dismissAlarm(activity, wakeLock);
                }
            }, Time.ONE_MINUTE);
        } else
            startCountdown();
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

    private void raiseHell() {
        hellMode = true;
        final boolean sleepCheckerOn = false;
        setDismissFragment(sleepCheckerOn);
        if (wakeLock.isHeld())
            wakeLock.release();
        alarm.playRingtone(activity, hellMode);
    }

    private void setSnoozeButtonPlaceholder() {
        FrameLayout snoozeButtonPlaceholder = findViewById(R.id.placeholder_snooze_button);
        if (sleepCheckerMode || hellMode || alarm.getSnoozeDuration() == SnoozeDuration.OFF)
            snoozeButtonPlaceholder.setVisibility(View.GONE);
        else {
            snoozeButtonPlaceholder.setVisibility(View.VISIBLE);
            setSnoozeFragment();
        }
    }

    private void setSnoozeFragment() {
        SnoozeFragment snoozeFragment = new SnoozeFragment();
        snoozeFragment.setOnViewInflatedListener(new OnViewInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
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
        dismissFragment.setOnViewInflatedListener(new OnViewInflatedListener() {
            @Override
            public void onViewInflated(Fragment fragment) {
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
        Bundle args = new Bundle();
        args.putBoolean(IntentExtra.SLEEP_CHECKER_ON.toString(), sleepCheckerOn);
        dismissFragment.setArguments(args);
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
                switch (secondsLeft) {
                    case 14:
                        countdownTextView.setTextColor(Color.parseColor("#009688")); // Green
                        break;
                    case 9:
                        countdownTextView.setTextColor(Color.parseColor("#FFC107")); // Amber
                        break;
                    case 4:
                        countdownTextView.setTextColor(Color.parseColor("#F44336")); // Red
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
