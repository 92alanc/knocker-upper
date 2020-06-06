package com.alancamargo.knockerupper.ui.viewmodel

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alancamargo.knockerupper.data.repository.AlarmRepository
import com.alancamargo.knockerupper.domain.model.Alarm
import com.alancamargo.knockerupper.domain.model.QueryResult
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import kotlinx.coroutines.launch

class AlarmViewModel(private val repository: AlarmRepository) : ViewModel() {

    private var stateLiveData = MutableLiveData<State>()

    init {
        fetchData()
    }

    fun getAlarms(): LiveData<State> = stateLiveData

    fun saveOrUpdate(alarm: Alarm) {
        viewModelScope.launch {
            repository.saveOrUpdate(alarm)
        }
    }

    fun delete(alarm: Alarm) {
        viewModelScope.launch {
            repository.delete(alarm)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            stateLiveData.postValue(State.Loading)
            val result = repository.getAlarms()
            val state = getStateFromResult(result)
            stateLiveData.postValue(state)
        }
    }

    private fun getStateFromResult(result: QueryResult<List<Alarm>>): State = when (result) {
        is QueryResult.Error -> State.Error
        is QueryResult.Success -> State.Success(result.body)
    }

    sealed class State : Parcelable {

        @Parcelize
        object Error : State()

        @Parcelize
        object Loading : State()

        @Parcelize
        data class Success(val alarms: @RawValue List<Alarm>) : State()

    }

}