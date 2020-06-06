package com.alancamargo.knockerupper.data.helpers.crashreport

interface CrashReportManager {

    fun log(message: String)

    fun log(t: Throwable)

}