package com.app.popular.ui.di

import com.app.auth.domain.repository.AuthRepository
import com.app.popular.domain.repository.PopularRepository
import com.app.popular.ui.viewModels.PopularViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val popularUiModule = module {

    viewModel<PopularViewModel> {
        PopularViewModel(
            get<PopularRepository>(),
            get<AuthRepository>()
        )
    }

}