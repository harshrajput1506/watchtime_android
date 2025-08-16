package com.app.media.ui.di

import com.app.media.domain.repository.MediaRepository
import com.app.media.ui.viewmodel.MediaDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mediaUiModule = module {

    viewModel { MediaDetailsViewModel(get<MediaRepository>()) }
}
