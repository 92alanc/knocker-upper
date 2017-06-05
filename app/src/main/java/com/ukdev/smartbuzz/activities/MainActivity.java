package com.ukdev.smartbuzz.activities;

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
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.backend.enums.Action;
import com.ukdev.smartbuzz.frontend.Utils;
import com.ukdev.smartbuzz.misc.LogTool;

/**
 * The main activity
 *
 * @author Alan Camargo
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton addButton = (FloatingActionButton)findViewById(R.id.addButton_Main);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetupActivity.class);
                intent.setAction(Action.CREATE_ALARM.toString());
                startActivity(intent);
            }
        });
        Utils.showAds(this, R.id.adView_Main);
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

}
