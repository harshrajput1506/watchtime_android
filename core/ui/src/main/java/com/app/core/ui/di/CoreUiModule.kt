package com.app.core.ui.di

import com.app.core.ui.theme.ThemePreferences
import com.app.core.ui.theme.ThemeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val coreUiModule = module {
    single { ThemePreferences(get()) }
    viewModel { ThemeViewModel(get()) }
}
