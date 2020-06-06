package com.alancamargo.knockerupper.di

import com.alancamargo.knockerupper.data.helpers.crashreport.CrashReportManager
import com.alancamargo.knockerupper.data.local.AlarmLocalDataSource
import com.alancamargo.knockerupper.data.repository.AlarmRepository
import com.alancamargo.knockerupper.data.repository.AlarmRepositoryImpl
import com.alancamargo.knockerupper.framework.helpers.crashreport.CrashReportManagerImpl
import com.alancamargo.knockerupper.framework.local.AlarmLocalDataSourceImpl
import com.alancamargo.knockerupper.framework.local.db.provider.AlarmDaoProvider
import com.alancamargo.knockerupper.ui.viewmodel.AlarmViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun getModules() = listOf(ui, data, framework)

private val ui = module {
    viewModel { AlarmViewModel(get()) }
}

private val data = module {
    factory<AlarmRepository> { AlarmRepositoryImpl(get()) }
    factory<AlarmLocalDataSource> { AlarmLocalDataSourceImpl(get(), get()) }
    factory<CrashReportManager> { CrashReportManagerImpl() }
}

private val framework = module {
    factory { AlarmDaoProvider.getInstance(androidContext()).provideDao() }
}