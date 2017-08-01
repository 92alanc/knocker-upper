package com.ukdev.smartbuzz.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import com.ukdev.smartbuzz.misc.IntentExtra;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.Time;
import com.ukdev.smartbuzz.system.AlarmHandler;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialiseComponents();
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recreate();
    }

    @Override
    public void onItemClick(View view, int position) {
        Alarm alarm = alarms.get(position);
        Intent intent = new Intent(context, SetupActivity.class);
        intent.putExtra(IntentExtra.EDIT_MODE.toString(), true);
        intent.putExtra(IntentExtra.ID.toString(), alarm.getId());
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
                Intent intent = new Intent(context, SetupActivity.class);
                intent.putExtra(IntentExtra.EDIT_MODE.toString(), false);
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

}
