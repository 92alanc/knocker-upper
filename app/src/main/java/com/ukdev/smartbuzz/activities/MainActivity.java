package com.ukdev.smartbuzz.activities;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ProgressBar;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.adapters.AlarmAdapter;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.listeners.OnItemClickListener;
import com.ukdev.smartbuzz.misc.IntentExtra;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.util.ViewUtils;

import java.util.List;

/**
 * The main activity
 *
 * @author Alan Camargo
 */
public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private Context context;
    private AlarmDao dao;
    private List<Alarm> alarms;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private OnItemClickListener listener;

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
    protected void onStop() {
        super.onStop();
        progressBar.setVisibility(View.GONE);
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
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.menuItem_About:
                ViewUtils.showAppInfo(context);
                break;
        }
        return super.onOptionsItemSelected(item);
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
        intent.putExtra(IntentExtra.EDIT_MODE.toString(), true);
        intent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        startActivity(intent);
    }

    private void initialiseComponents() {
        context = this;
        dao = AlarmDao.getInstance(context);
        listener = this;
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
    }

    private void setAddButton() {
        FloatingActionButton addButton = (FloatingActionButton)findViewById(R.id.fab_main);
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
        alarms = dao.select();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        AlarmAdapter adapter = new AlarmAdapter(context, alarms, listener);
        recyclerView.setAdapter(adapter);
    }

}
