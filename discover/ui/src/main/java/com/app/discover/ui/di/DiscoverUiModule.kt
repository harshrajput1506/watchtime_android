package com.app.discover.ui.di

import com.app.discover.ui.viewModels.DiscoverViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val discoverUiModule = module {
    viewModel<DiscoverViewModel> {
        DiscoverViewModel(get())
    }
}
