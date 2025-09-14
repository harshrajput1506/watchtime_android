package com.app.media.ui.di

import com.app.collections.domain.repository.CollectionRepository
import com.app.media.domain.repository.MediaRepository
import com.app.media.ui.viewmodel.MediaDetailsViewModel
import com.app.media.ui.viewmodel.SeasonViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mediaUiModule = module {

    viewModel { (mediaId: Int, mediaType: String) ->
        MediaDetailsViewModel(
            mediaId,
            mediaType,
            get<MediaRepository>(),
            get<CollectionRepository>()
        )
    }
    viewModel { SeasonViewModel(get<MediaRepository>()) }
}
