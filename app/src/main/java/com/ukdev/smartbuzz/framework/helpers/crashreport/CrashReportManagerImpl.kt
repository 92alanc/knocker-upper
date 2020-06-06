package com.ukdev.smartbuzz.framework.helpers.crashreport

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ukdev.smartbuzz.data.helpers.crashreport.CrashReportManager

class CrashReportManagerImpl : CrashReportManager {

    override fun log(message: String) {
        Log.d(TAG, message)
        FirebaseCrashlytics.getInstance().log(message)
    }

    override fun log(t: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(t)
    }

    private companion object {
        const val TAG = "LOG_ALAN"
    }

}