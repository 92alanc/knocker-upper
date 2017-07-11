package com.ukdev.smartbuzz.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.fragments.DismissFragment;
import com.ukdev.smartbuzz.fragments.SnoozeFragment;
import com.ukdev.smartbuzz.misc.IntentAction;
import com.ukdev.smartbuzz.misc.IntentExtra;

/**
 * The activity where alarms and
 * Sleep Checker are triggered
 *
 * @author Alan Camargo
 */
public class AlarmActivity extends AppCompatActivity {

    private boolean sleepCheckerMode;
    private Context context;
    private DismissFragment dismissFragment;
    private FrameLayout snoozeButtonPlaceholder;
    private SnoozeFragment snoozeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        initialiseComponents();
    }

    private void initialiseComponents() {
        String action = IntentAction.TRIGGER_SLEEP_CHECKER.toString();
        sleepCheckerMode = getIntent().getAction().equals(action);
        context = this;
        setSnoozeButtonPlaceholder();
        setDismissFragment();
    }

    private void setSnoozeButtonPlaceholder() {
        snoozeButtonPlaceholder = (FrameLayout) findViewById(R.id.placeholder_snooze_button);
        if (sleepCheckerMode)
            snoozeButtonPlaceholder.setVisibility(View.GONE);
        else
            setSnoozeFragment();
    }

    private void setSnoozeFragment() {
        snoozeFragment = new SnoozeFragment();
    }

    private void setDismissFragment() {
        dismissFragment = new DismissFragment();
        Bundle args = new Bundle();
        args.putBoolean(IntentExtra.SLEEP_CHECKER_ON.toString(), sleepCheckerMode);
        dismissFragment.setArguments(args);
    }

}
