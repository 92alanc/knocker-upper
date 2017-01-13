package com.ukdev.smartbuzz.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.backend.AlarmHandler;
import com.ukdev.smartbuzz.backend.BackEndTools;
import com.ukdev.smartbuzz.database.AlarmRepository;
import com.ukdev.smartbuzz.extras.AppConstants;
import com.ukdev.smartbuzz.frontend.FrontEndTools;
import com.ukdev.smartbuzz.model.Alarm;

import java.util.ArrayList;

/**
 * Home screen
 * Created by Alan Camargo - March 2016
 */
public class HomeActivity extends AppCompatActivity
{

    private boolean isFabOpen = false;
    private ListView listView;
    private ArrayList<Integer> selectedItems;
    private Toolbar toolbar;
    private AlarmRepository database;
    private Animation rotate_forward, rotate_backward, fab_open, fab_close;
    private FloatingActionButton addButton, addAlarmButton, addReminderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        database = AlarmRepository.getInstance(this);
        addAlarmButton = (FloatingActionButton) findViewById(R.id.addAlarmButton);
        addAlarmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createAlarm(false);
            }
        });
        addReminderButton = (FloatingActionButton) findViewById(R.id.addReminderButton);
        addReminderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createAlarm(true);
            }
        });
        setAddButton();
        setListView();
        FrontEndTools.showAds(this, R.id.homeAdView);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        BackEndTools.killApp(this);
    }

    @Override
    public void onBackPressed()
    {
        BackEndTools.killApp(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.aboutItem:
                showInfo();
                break;
            case R.id.helpItem:
                FrontEndTools.startActivity(HomeActivity.this, HelpActivity.class);
                break;
            case R.id.sortTimeItem:
                if (database.getRowCount() > 1)
                    FrontEndTools.adaptAlarmsListView(this, listView, AppConstants.ORDER_BY_TIME);
                break;
            case R.id.sortTitleItem:
                if (database.getRowCount() > 1)
                    FrontEndTools.adaptAlarmsListView(this, listView, AppConstants.ORDER_BY_TITLE);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When the activity is resumed
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        setListView();
    }

    /**
     * Shows app info
     */
    private void showInfo()
    {
        try
        {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String version = info.versionName;
            FrontEndTools.showDialogue(this, getString(R.string.app_name) + " "
                            + version,
                    getString(R.string.info), R.drawable.app_icon,
                    getString(R.string.ok), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            // Do nothing
                        }
                    });
        } catch (PackageManager.NameNotFoundException e)
        {
            // Damn! Something really wrong happened here
            e.printStackTrace();
        }
    }

    /**
     * Sets actions to addButton
     */
    private void setAddButton()
    {
        addButton =
                (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        animateButton();
                    }
                }
        );
    }

    private void animateButton()
    {
        TextView addAlarmText = (TextView) findViewById(R.id.addAlarmText);
        TextView addReminderText = (TextView) findViewById(R.id.addReminderText);
        if (isFabOpen)
        {
            addButton.startAnimation(rotate_backward);
            addAlarmButton.startAnimation(fab_close);
            addAlarmText.startAnimation(fab_close);
            addReminderButton.startAnimation(fab_close);
            addReminderText.startAnimation(fab_close);
            addAlarmButton.setClickable(false);
            addReminderButton.setClickable(false);
            isFabOpen = false;
        }
        else
        {
            addButton.startAnimation(rotate_forward);
            addAlarmButton.startAnimation(fab_open);
            addAlarmText.startAnimation(fab_open);
            addReminderButton.startAnimation(fab_open);
            addReminderText.startAnimation(fab_open);
            addAlarmButton.setClickable(true);
            addReminderButton.setClickable(true);
            isFabOpen = true;
        }
    }

    /**
     * Creates an alarm or a reminder
     */
    private void createAlarm(boolean reminder)
    {
        Intent intent = new Intent(HomeActivity.this, AlarmCreatorActivity.class);
        intent.setAction(AppConstants.ACTION_CREATE_ALARM);
        intent.putExtra(AppConstants.EXTRA_REMINDER, reminder);
        startActivity(intent);
    }

    /**
     * Sets actions to listView
     */
    private void setListView()
    {
        listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        selectedItems = new ArrayList<>();
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener()
        {
            int i = 0;

            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode actionMode,
                                                  int position,
                                                  long l, boolean isChecked)
            {
                i = position;
                if (isChecked)
                {
                    if (!selectedItems.contains(position))
                        selectedItems.add(position);
                    if (listView.getChildAt(position) != null)
                        listView.getChildAt(position).setBackgroundResource(
                                R.drawable.alarm_listviewitem_shape_selected);
                }
                else
                {
                    if (selectedItems.contains(position))
                        selectedItems.remove(selectedItems.indexOf(position));
                    if (listView.getChildAt(position) != null)
                        listView.getChildAt(position).setBackgroundResource(R.drawable.alarm_listviewitem_shape);
                }
                if (selectedItems.size() == 1)
                    actionMode.setTitle(getString(R.string.item_selected));
                else
                    actionMode.setTitle(String.format(getString(R.string.items_selected),
                            selectedItems.size()));
            }

            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu)
            {
                closeOptionsMenu();
                toolbar.setVisibility(View.GONE);
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_action, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu)
            {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.aboutItem:
                        showInfo();
                        mode.finish();
                        return true;
                    case R.id.deleteItem:
                        deleteSelectedAlarms();
                        mode.finish();
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode)
            {
                selectedItems.clear();
                toolbar.setVisibility(View.VISIBLE);
                for (int i = 0; i < listView.getChildCount(); i++)
                    listView.getChildAt(i).setBackgroundResource(R.drawable.alarm_listviewitem_shape);
            }
        });
        FrontEndTools.adaptAlarmsListView(this, listView, AppConstants.ORDER_BY_ID);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i,
                                    long l)
            {
                Alarm alarm = (Alarm) listView.getItemAtPosition(i);
                Intent intent = new Intent(HomeActivity.this, AlarmCreatorActivity.class);
                intent.setAction(AppConstants.ACTION_EDIT_ALARM);
                intent.putExtra(AppConstants.EXTRA_EDIT, alarm.getId());
                intent.putExtra(AppConstants.EXTRA_REMINDER, alarm.isReminder());
                startActivity(intent);
            }
        });
        FrontEndTools.showNotification(this);
    }

    /**
     * Deletes the selected alarm(s)
     */
    private void deleteSelectedAlarms()
    {
        Alarm alarm = null;
        for (int position : selectedItems)
        {
            alarm = (Alarm) listView.getItemAtPosition(position);
            AlarmHandler.cancelAlarm(getBaseContext(), alarm);
            database.delete(alarm.getId());
        }
        if (alarm != null)
        {
            listView.invalidate();
            FrontEndTools.adaptAlarmsListView(HomeActivity.this,
                    listView, AppConstants.ORDER_BY_ID);
            FrontEndTools.showNotification(this);
            String toastText;
            if (selectedItems.size() == 1)
                toastText = getString(R.string.alarm_deleted);
            else
                toastText = String.format(getString(R.string.alarms_deleted),
                        selectedItems.size());
            FrontEndTools.showToast(getBaseContext(),
                    toastText,
                    Toast.LENGTH_SHORT);
        }
    }

}