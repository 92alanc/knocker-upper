package com.ukdev.smartbuzz.di

import com.ukdev.smartbuzz.ui.viewmodel.AlarmViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun getModules() = listOf(ui)

private val ui = module {
    viewModel { AlarmViewModel() }
}