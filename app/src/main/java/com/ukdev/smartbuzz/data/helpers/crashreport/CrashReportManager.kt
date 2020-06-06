package com.ukdev.smartbuzz.data.helpers.crashreport

interface CrashReportManager {

    fun log(message: String)

    fun log(t: Throwable)

}