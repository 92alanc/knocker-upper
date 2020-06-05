package com.ukdev.smartbuzz.ui.activities.state

import com.ukdev.smartbuzz.domain.model.Alarm

sealed class MainActivityState {

    object Error : MainActivityState()

    object Loading : MainActivityState()

    data class Success(val body: Alarm) : MainActivityState()

}