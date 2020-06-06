package com.ukdev.smartbuzz.di

import com.ukdev.smartbuzz.data.repository.AlarmRepository
import com.ukdev.smartbuzz.data.repository.AlarmRepositoryImpl
import com.ukdev.smartbuzz.ui.viewmodel.AlarmViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun getModules() = listOf(ui, data)

private val ui = module {
    viewModel { AlarmViewModel(get()) }
}

private val data = module {
    factory<AlarmRepository> { AlarmRepositoryImpl() }
}