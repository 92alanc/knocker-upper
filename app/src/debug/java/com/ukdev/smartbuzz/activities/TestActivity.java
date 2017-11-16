package com.ukdev.smartbuzz.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.ukdev.smartbuzz.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout container = new FrameLayout(this);
        container.setId(R.id.container);
        setContentView(container);
    }

}
