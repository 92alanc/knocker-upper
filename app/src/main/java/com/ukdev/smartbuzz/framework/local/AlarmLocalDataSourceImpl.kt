package com.ukdev.smartbuzz.framework.local

import com.ukdev.smartbuzz.data.local.AlarmLocalDataSource
import com.ukdev.smartbuzz.domain.model.Alarm
import com.ukdev.smartbuzz.domain.model.QueryResult

class AlarmLocalDataSourceImpl : AlarmLocalDataSource {

    override suspend fun getAlarms(): QueryResult<List<Alarm>> {
        TODO("not implemented")
    }

}