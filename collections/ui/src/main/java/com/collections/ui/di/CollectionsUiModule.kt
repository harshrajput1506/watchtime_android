package com.collections.ui.di

import com.app.auth.domain.repository.AuthRepository
import com.app.collections.domain.repository.CollectionRepository
import com.collections.ui.viewModels.CollectionsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val collectionsUiModule = module {

    viewModel<CollectionsViewModel> {
        CollectionsViewModel(
            get<CollectionRepository>(),
            get<AuthRepository>()
        )
    }

}
