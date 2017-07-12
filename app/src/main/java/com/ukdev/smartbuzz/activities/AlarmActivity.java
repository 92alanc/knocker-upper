package com.ukdev.smartbuzz.activities;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.fragments.DismissFragment;
import com.ukdev.smartbuzz.fragments.SnoozeFragment;
import com.ukdev.smartbuzz.misc.IntentAction;
import com.ukdev.smartbuzz.misc.IntentExtra;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.system.AlarmHandler;
import com.ukdev.smartbuzz.util.Utils;

/**
 * The activity where alarms and
 * Sleep Checker are triggered
 *
 * @author Alan Camargo
 */
public class AlarmActivity extends AppCompatActivity {

    private Activity activity;
    private Alarm alarm;
    private AlarmHandler alarmHandler;
    private boolean hellMode;
    private boolean sleepCheckerMode;
    private Context context;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        initialiseComponents();
    }

    @Override
    public void onBackPressed() { }

    private void countdown() {
        // TODO: implement Sleep Checker countdown
    }

    private void initialiseComponents() {
        activity = this;
        context = this;
        String sleepCheckerAction = IntentAction.TRIGGER_SLEEP_CHECKER.toString();
        sleepCheckerMode = getIntent().getAction().equals(sleepCheckerAction);
        String hellAction = IntentAction.HELL.toString();
        hellMode = getIntent().getAction().equals(hellAction);
        AlarmDao dao = AlarmDao.getInstance(context);
        int alarmId = getIntent().getIntExtra(IntentExtra.ID.toString(), 0);
        alarm = dao.select(alarmId);
        alarmHandler = new AlarmHandler(context, alarm);
        mediaPlayer = new MediaPlayer();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (hellMode)
            raiseHell();
        if (!sleepCheckerMode) {
            Utils.playRingtone(activity, mediaPlayer, alarm.getVolume(), alarm.getRingtoneUri());
            if (alarm.vibrates())
                Utils.startVibration(vibrator);
        } else
            countdown();
        setSnoozeButtonPlaceholder();
        setDismissFragment();
    }

    private void raiseHell() {
        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Uri ringtone = alarm.getRingtoneUri();
        if (ringtone.equals(defaultRingtoneUri))
            ringtone = RingtoneManager.getValidRingtoneUri(context);
        int volume = Utils.getMaxVolume(context);
        Utils.playRingtone(activity, mediaPlayer, volume, ringtone);
        Utils.startVibration(vibrator);
    }

    private void setSnoozeButtonPlaceholder() {
        FrameLayout snoozeButtonPlaceholder = (FrameLayout) findViewById(R.id.placeholder_snooze_button);
        if (sleepCheckerMode || hellMode)
            snoozeButtonPlaceholder.setVisibility(View.GONE);
        else
            setSnoozeFragment();
    }

    private void setSnoozeFragment() {
        SnoozeFragment snoozeFragment = new SnoozeFragment();
        snoozeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.stopRingtone(mediaPlayer);
                if (alarm.vibrates() || hellMode)
                    Utils.stopVibration(vibrator);
                alarmHandler.delayAlarm();
            }
        });
    }

    private void setDismissFragment() {
        DismissFragment dismissFragment = new DismissFragment();
        Bundle args = new Bundle();
        args.putBoolean(IntentExtra.SLEEP_CHECKER_ON.toString(), sleepCheckerMode);
        dismissFragment.setArguments(args);
        dismissFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sleepCheckerMode)
                    Utils.killApp(activity);
                else {
                    Utils.stopRingtone(mediaPlayer);
                    if (alarm.vibrates() || hellMode)
                        Utils.stopVibration(vibrator);
                    alarmHandler.dismissAlarm(activity, null); // TODO: replace null with wakeLock
                }
            }
        });
    }

}
