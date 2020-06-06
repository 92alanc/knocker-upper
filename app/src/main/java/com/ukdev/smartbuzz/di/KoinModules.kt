package com.ukdev.smartbuzz.di

import com.ukdev.smartbuzz.data.local.AlarmLocalDataSource
import com.ukdev.smartbuzz.data.repository.AlarmRepository
import com.ukdev.smartbuzz.data.repository.AlarmRepositoryImpl
import com.ukdev.smartbuzz.framework.local.AlarmLocalDataSourceImpl
import com.ukdev.smartbuzz.framework.local.db.provider.AlarmDaoProvider
import com.ukdev.smartbuzz.ui.viewmodel.AlarmViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun getModules() = listOf(ui, data, framework)

private val ui = module {
    viewModel { AlarmViewModel(get()) }
}

private val data = module {
    factory<AlarmRepository> { AlarmRepositoryImpl(get()) }
    factory<AlarmLocalDataSource> { AlarmLocalDataSourceImpl(get()) }
}

private val framework = module {
    factory { AlarmDaoProvider.getInstance(androidContext()).provideDao() }
}