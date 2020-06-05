package com.ukdev.smartbuzz.ui.viewmodel.state

import android.os.Parcelable
import com.ukdev.smartbuzz.domain.model.Alarm
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

sealed class AlarmViewModelState : Parcelable {

    @Parcelize
    object Error : AlarmViewModelState()

    @Parcelize
    object Loading : AlarmViewModelState()

    @Parcelize
    data class Success(val alarms: @RawValue List<Alarm>) : AlarmViewModelState()

}