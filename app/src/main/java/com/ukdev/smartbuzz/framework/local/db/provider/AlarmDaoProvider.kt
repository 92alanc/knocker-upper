package com.ukdev.smartbuzz.framework.local.db.provider

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ukdev.smartbuzz.framework.local.db.AlarmDao
import com.ukdev.smartbuzz.framework.local.model.DbAlarm

@Database(entities = [DbAlarm::class], version = 1, exportSchema = false)
abstract class AlarmDaoProvider : RoomDatabase() {

    abstract fun provideDao(): AlarmDao

    companion object {
        private const val DB_NAME = "alarm_database"

        private var instance: AlarmDaoProvider? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): AlarmDaoProvider {
            if (instance == null) {
                instance = Room.databaseBuilder(
                        context,
                        AlarmDaoProvider::class.java,
                        DB_NAME
                ).fallbackToDestructiveMigration().build()
            }

            return instance!!
        }
    }

}