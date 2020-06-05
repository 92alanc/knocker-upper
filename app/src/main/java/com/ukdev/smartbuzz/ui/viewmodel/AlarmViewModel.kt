package com.ukdev.smartbuzz.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukdev.smartbuzz.ui.viewmodel.state.AlarmViewModelState
import kotlinx.coroutines.launch

class AlarmViewModel : ViewModel() {

    private var state: AlarmViewModelState = AlarmViewModelState.Success(emptyList())
    private var stateLiveData = MutableLiveData<AlarmViewModelState>()

    init {
        fetchData()
    }

    fun getAlarms(): LiveData<AlarmViewModelState> = stateLiveData

    private fun fetchData() {
        viewModelScope.launch {
            // TODO: use correct data
            state = AlarmViewModelState.Success(emptyList())
            stateLiveData.postValue(state)
        }
    }

}