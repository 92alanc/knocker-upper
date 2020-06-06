package com.ukdev.smartbuzz.data.repository

import com.ukdev.smartbuzz.domain.model.Alarm
import com.ukdev.smartbuzz.domain.model.QueryResult

class AlarmRepositoryImpl : AlarmRepository {

    override suspend fun getAlarms(): QueryResult<List<Alarm>> {
        TODO("not implemented")
    }

}