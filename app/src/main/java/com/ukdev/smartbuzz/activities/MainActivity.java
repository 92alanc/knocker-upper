package com.ukdev.smartbuzz.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.misc.LogTool;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
