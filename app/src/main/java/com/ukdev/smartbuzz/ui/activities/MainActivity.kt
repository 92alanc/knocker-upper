package com.ukdev.smartbuzz.ui.activities

import androidx.appcompat.app.AppCompatActivity
import com.ukdev.smartbuzz.R
import com.ukdev.smartbuzz.ui.viewmodel.AlarmViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by viewModel<AlarmViewModel>()

}