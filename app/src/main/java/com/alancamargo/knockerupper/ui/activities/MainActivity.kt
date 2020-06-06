package com.alancamargo.knockerupper.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alancamargo.knockerupper.R
import com.alancamargo.knockerupper.domain.model.Alarm
import com.alancamargo.knockerupper.ui.adapter.AlarmAdapter
import com.alancamargo.knockerupper.ui.tools.hide
import com.alancamargo.knockerupper.ui.tools.show
import com.alancamargo.knockerupper.ui.viewmodel.AlarmViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by viewModel<AlarmViewModel>()
    private val adapter = AlarmAdapter()

    private lateinit var viewModelState: AlarmViewModel.State

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerView.adapter = adapter
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
        return savedInstanceState?.getParcelable<AlarmViewModel.State>(KEY_VIEW_MODEL_STATE)?.let {
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

    private fun handleViewModelState(viewModelState: AlarmViewModel.State?) {
        when (viewModelState) {
            is AlarmViewModel.State.Error -> handleError()
            is AlarmViewModel.State.Loading -> handleLoading()
            is AlarmViewModel.State.Success -> handleSuccess(viewModelState.alarms)
        }
    }

    private fun handleError() {
        progressBar.hide()
        // TODO: show error
    }

    private fun handleLoading() {
        recyclerView.hide()
        progressBar.show()
    }

    private fun handleSuccess(alarms: List<Alarm>) {
        progressBar.hide()
        recyclerView.show()
        adapter.submitList(alarms)
    }

    private companion object {
        const val KEY_VIEW_MODEL_STATE = "view-model-state"
    }

}