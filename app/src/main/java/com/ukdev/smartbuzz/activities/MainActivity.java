package com.ukdev.smartbuzz.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.AlarmClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.adapters.AlarmAdapter;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.listeners.OnItemClickListener;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.Time;
import com.ukdev.smartbuzz.util.AlarmHandler;
import com.ukdev.smartbuzz.util.PreferenceUtils;
import com.ukdev.smartbuzz.util.Utils;
import com.ukdev.smartbuzz.util.ViewUtils;

import java.util.List;

/**
 * The main activity
 *
 * @author Alan Camargo
 */
public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private AlarmDao dao;
    private Context context;
    private ImageView noAlarmsImageView;
    private int backPressedCount;
    private List<Alarm> alarms;
    private OnItemClickListener listener;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView noAlarmsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(PreferenceUtils.FILE_NAME,
                                                                   MODE_PRIVATE);
        PreferenceUtils preferenceUtils = new PreferenceUtils(this, sharedPreferences);
        setTheme(preferenceUtils.getTheme().getRes());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialiseComponents();
        checkForVersionChange();
        checkForVoiceCommand();
        setAddButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backPressedCount = 0;
        alarms = dao.select();
        if (alarms.isEmpty()) {
            noAlarmsImageView.setVisibility(View.VISIBLE);
            noAlarmsTextView.setVisibility(View.VISIBLE);
        } else {
            noAlarmsImageView.setVisibility(View.GONE);
            noAlarmsTextView.setVisibility(View.GONE);
        }
        populateRecyclerView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        backPressedCount = 0;
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        backPressedCount++;
        if (backPressedCount == 2)
            Utils.killApp(this);
        else {
            Toast.makeText(context, R.string.tap_back_again, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressedCount--;
                }
            }, Time.TWO_SECONDS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_about:
                ViewUtils.showAppInfo(context);
                break;
            case R.id.item_rate:
                openAppPage();
                break;
            case R.id.item_settings:
                openSettings();
                break;
            case R.id.item_share:
                shareApp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recreate();
    }

    @Override
    public void onItemClick(int position) {
        Alarm alarm = alarms.get(position);
        Intent intent = SetupActivity.getIntent(this, alarm.getId());
        startActivity(intent);
    }

    private void initialiseComponents() {
        context = this;
        dao = AlarmDao.getInstance(context);
        listener = this;
        noAlarmsImageView = findViewById(R.id.image_view_no_alarms);
        noAlarmsTextView = findViewById(R.id.text_view_no_alarms);
        progressBar = findViewById(R.id.progress_bar_main);
        recyclerView = findViewById(R.id.recycler_view_main);
    }

    private void setAddButton() {
        FloatingActionButton addButton = findViewById(R.id.fab_main);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = SetupActivity.getIntent(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private void populateRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        AlarmAdapter adapter = new AlarmAdapter(context, alarms, listener);
        recyclerView.setAdapter(adapter);
    }

    private void checkForVoiceCommand() {
        Intent intent = getIntent();
        if (AlarmClock.ACTION_SET_ALARM.equals(intent.getAction()))
            AlarmHandler.setAlarmByVoice(intent, this);
    }

    private void checkForVersionChange() {
        if (Utils.hasChangedAppVersion(this)) {
            for (Alarm alarm : dao.getActiveAlarms()) {
                AlarmHandler handler = new AlarmHandler(this, alarm);
                handler.setAlarm();
            }
            Utils.updateAppVersion(this);
        }
    }

    private void openAppPage() {
        Uri uri;
        uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        int flags;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        } else
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        goToMarket.addFlags(flags);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            uri = Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private void openSettings() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        String text = String.format("https://play.google.com/store/apps/details?id=%s",
                                    getPackageName());
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

}
