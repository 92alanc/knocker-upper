package com.ukdev.smartbuzz.common

import android.support.test.InstrumentationRegistry
import com.ukdev.smartbuzz.database.AlarmDao

open class BaseTest {

    val context = InstrumentationRegistry.getTargetContext()!!

    fun clearDatabase() {
        val dao = getDao()
        val alarms = dao.select()
        alarms.filter { it.id != 1 }
              .forEach { dao.delete(it) }
    }

    fun getDao(): AlarmDao = AlarmDao.getInstance(context)

}