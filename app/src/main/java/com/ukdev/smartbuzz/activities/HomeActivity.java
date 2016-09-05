package com.ukdev.smartbuzz.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.ukdev.smartbuzz.database.AlarmDAO;
import com.ukdev.smartbuzz.extras.AlarmHandler;
import com.ukdev.smartbuzz.extras.AppConstants;
import com.ukdev.smartbuzz.extras.FrontEndTools;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.extras.BackEndTools;
import com.ukdev.smartbuzz.model.Alarm;

import java.util.ArrayList;

/**
 * Home screen
 * Created by Alan Camargo - March 2016
 */
public class HomeActivity extends AppCompatActivity
{

    private ListView listView;
    private ArrayList<Integer> selectedItems;
    private Toolbar toolbar;
    private AlarmDAO database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = AlarmDAO.getInstance(this);
        setAddButton();
        setListView();
        if (AppConstants.OS_VERSION >= Build.VERSION_CODES.M)
            BackEndTools.requestPermissions(this, HomeActivity.this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        FrontEndTools.killApp(this);
    }

    @Override
    public void onBackPressed()
    {
        FrontEndTools.killApp(this);
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
                if (database.selectAll(this, AppConstants.ORDER_BY_TIME).length > 1)
                    FrontEndTools.adaptAlarmsListView(this, listView, AppConstants.ORDER_BY_TIME);
                break;
            case R.id.sortTitleItem:
                if (database.selectAll(this, AppConstants.ORDER_BY_TITLE).length > 1)
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
        }
        catch (PackageManager.NameNotFoundException e)
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
        FloatingActionButton addButton =
                (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(HomeActivity.this, AlarmCreatorActivity.class);
                        intent.setAction(AppConstants.ACTION_CREATE_ALARM);
                        startActivity(intent);
                    }
                }
        );
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
                    listView.getChildAt(position).setBackgroundResource(R.drawable.alarm_listviewitem_shape_selected);
                }
                else
                {
                    if (selectedItems.contains(position))
                        selectedItems.remove(selectedItems.indexOf(position));
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
                Alarm alarm = (Alarm)listView.getItemAtPosition(i);
                if (alarm.isLocked())
                    FrontEndTools.showToast(getBaseContext(),
                            String.format(getString(R.string.alarm_locked),
                            alarm.getTitle()), Toast.LENGTH_LONG);
                else
                {
                    Intent intent = new Intent(HomeActivity.this, AlarmCreatorActivity.class);
                    intent.setAction(AppConstants.ACTION_EDIT_ALARM);
                    intent.putExtra(AppConstants.EXTRA_EDIT, ((Alarm)listView.getItemAtPosition(i)).getId());
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Deletes the selected alarm(s)
     */
    private void deleteSelectedAlarms()
    {
        Alarm alarm = null;
        for (int position : selectedItems)
        {
            alarm = (Alarm)listView.getItemAtPosition(position);
            if (alarm.isLocked())
                FrontEndTools.showToast(this, String.format(getString(R.string.alarm_locked),
                        alarm.getTitle()),
                        Toast.LENGTH_LONG);
            else
            {
                AlarmHandler.cancelAlarm(getBaseContext(), alarm);
                database.delete(alarm.getId());
            }
        }
        if (alarm != null && !alarm.isLocked())
        {
            listView.invalidate();
            FrontEndTools.adaptAlarmsListView(HomeActivity.this,
                    listView, AppConstants.ORDER_BY_ID);
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