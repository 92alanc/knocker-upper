package com.ukdev.smartbuzz.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.frontend.FrontEndTools;

/**
 * Help activity
 * Provides help for various topics of the app
 * Created by Alan Camargo - June 2016
 */
public class HelpActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setListView();
        FrontEndTools.showAds(this, R.id.helpAdView);
    }

    /**
     * Populates the list view
     * Sets actions to listView
     */
    private void setListView()
    {
        // First, we'll populate the list view
        ListView listView = (ListView) findViewById(R.id.helpListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        final String[] helpTopics = getResources().getStringArray(R.array.helpTopics);
        adapter.addAll(helpTopics);
        listView.setAdapter(adapter);

        // After, we'll set the onClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long l)
            {
                String[] helpContents = getResources().getStringArray(R.array.helpContents);
                String title = helpTopics[position];
                String text = helpContents[position];
                AlertDialog dialogue = new AlertDialog.Builder(HelpActivity.this).create();
                dialogue.setTitle(title);
                dialogue.setMessage(text);
                dialogue.setIcon(R.drawable.help);
                dialogue.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.ok),
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                // Do nothing
                            }
                        });
                dialogue.show();
            }
        });
    }

}
