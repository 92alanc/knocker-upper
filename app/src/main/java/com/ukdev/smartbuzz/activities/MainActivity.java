package com.ukdev.smartbuzz.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.adapters.AlarmAdapter;
import com.ukdev.smartbuzz.misc.IntentAction;
import com.ukdev.smartbuzz.misc.IntentExtra;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.listeners.RecyclerViewClickListener;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.util.ViewUtils;
import com.ukdev.smartbuzz.misc.LogTool;

import java.util.List;

/**
 * The main activity
 *
 * @author Alan Camargo
 */
public class MainActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private Context context;
    private AlarmDao dao;
    private List<Alarm> alarms;
    private RecyclerView recyclerView;
    private RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialiseComponents();
        setAddButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateRecyclerView();
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
            case R.id.menuItem_Settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.menuItem_About:
                showInfo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialiseComponents() {
        context = this;
        dao = AlarmDao.getInstance(context);
        listener = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        ViewUtils.showAds(this, R.id.ad_view_main);
    }

    private void setAddButton() {
        FloatingActionButton addButton = (FloatingActionButton)findViewById(R.id.addButton_Main);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SetupActivity.class);
                intent.setAction(IntentAction.CREATE_ALARM.toString());
                startActivity(intent);
            }
        });
    }

    private void populateRecyclerView() {
        alarms = dao.select();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        AlarmAdapter adapter = new AlarmAdapter(context, alarms, listener);
        recyclerView.setAdapter(adapter);
    }

    private void showInfo() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String version = info.versionName;
            String appName = getString(R.string.app_name);
            String about = getString(R.string.about);
            String text = String.format("%1$s %2$s\n%3$s", appName, version, about);
            LogTool log = new LogTool(this);
            log.info(text);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method called when a recycler view item is clicked
     * @param view     the clicked view
     * @param position the clicked position
     */
    @Override
    public void onItemClick(View view, int position) {
        Alarm alarm = alarms.get(position);
        Intent intent = new Intent(context, SetupActivity.class);
        intent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        startActivity(intent);
    }

}
