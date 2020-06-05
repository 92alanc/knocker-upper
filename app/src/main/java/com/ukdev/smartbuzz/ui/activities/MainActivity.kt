package com.ukdev.smartbuzz.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ukdev.smartbuzz.R
import com.ukdev.smartbuzz.domain.model.Alarm
import com.ukdev.smartbuzz.ui.viewmodel.state.AlarmViewModelState
import com.ukdev.smartbuzz.ui.viewmodel.AlarmViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by viewModel<AlarmViewModel>()

    private lateinit var viewModelState: AlarmViewModelState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadViewModelStateFromSavedInstanceState(savedInstanceState) ?: observeData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_VIEW_MODEL_STATE, viewModelState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        loadViewModelStateFromSavedInstanceState(savedInstanceState)
    }

    private fun loadViewModelStateFromSavedInstanceState(savedInstanceState: Bundle?): Unit? {
        return savedInstanceState?.getParcelable<AlarmViewModelState>(KEY_VIEW_MODEL_STATE)?.let {
            viewModelState = it
            handleViewModelState(viewModelState)
        }
    }

    private fun observeData() {
        viewModel.getAlarms().observe(this, Observer { viewModelState ->
            this.viewModelState = viewModelState
            handleViewModelState(viewModelState)
        })
    }

    private fun handleViewModelState(viewModelState: AlarmViewModelState?) {
        when (viewModelState) {
            is AlarmViewModelState.Error -> handleError()
            is AlarmViewModelState.Loading -> handleLoading()
            is AlarmViewModelState.Success -> handleSuccess(viewModelState.alarms)
        }
    }

    private fun handleError() {
        TODO("not implemented")
    }

    private fun handleLoading() {
        TODO("not implemented")
    }

    private fun handleSuccess(alarms: List<Alarm>) {
        TODO("not implemented")
    }

    private companion object {
        const val KEY_VIEW_MODEL_STATE = "view-model-state"
    }

}